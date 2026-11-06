package uniandes.dpoo.hamburguesas.tests;

import uniandes.dpoo.hamburguesas.excepciones.HamburguesaException;
import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;


public class RestauranteTest {

	
	private Restaurante restaurante;
	private File ingredientes;
	private File menu;
	private File combos;

	
	public void setup() {
		restaurante = new Restaurante();
		ingredientes = new File("./data/ingredientes.txt");
		menu = new File("./data/menu.txt");
		combos = new File("./data/combos.txt");
	}


	@Test
	public void restauranteVacioTest() {
		setup();
		assertNull(restaurante.getPedidoEnCurso(), "No debe haber pedido en curso");
		assertTrue(restaurante.getPedidos().isEmpty(), "No debe haber pedidos cen el historial");
		assertTrue(restaurante.getIngredientes().isEmpty(), "No debe haber ingredientes");
		assertTrue(restaurante.getMenuBase().isEmpty(), "No debe haber productos en el menú");
		assertTrue(restaurante.getMenuCombos().isEmpty(), "No debe haber combos");
	}

	@Test
	public void iniciarPedidoTest() {
		setup();
		try {
			restaurante.iniciarPedido("Ana Lopez", "Calle 123");
		}
		catch (YaHayUnPedidoEnCursoException e) {
			fail("No debió lanzar exception");
		}
		assertNotNull(restaurante.getPedidoEnCurso(), "Debió crear el pedido en curso");
		assertEquals("Ana Lopez", restaurante.getPedidoEnCurso().getNombreCliente(), "El cliente no es correcto");
	}

	@Test
	public void iniciarPedidoYaExistenteTest() {
		setup();
		try {
			restaurante.iniciarPedido("Ana Lopez", "Calle 123");
			restaurante.iniciarPedido("Luis Gomez", "Carrera 7");
			fail("Debió lanzar YaHayUnPedidoEnCursoException");
		}
		catch (YaHayUnPedidoEnCursoException e) {
			assertTrue(e.getMessage().contains("Ana Lopez"), "El mensaje debe incluir el cliente del pedido en curso");
			assertTrue(e.getMessage().contains("Luis Gomez"), "El mensaje debe incluir el nuevo cliente");
			assertEquals("Ana Lopez", restaurante.getPedidoEnCurso().getNombreCliente(),
					"El pedido en curso no debió cambiar");
		}
	}

	@Test
	public void cerrarYGuardarPedidoTest() {
		setup();
		new File("./facturas/").mkdirs();
		File factura = null;
		try {
			restaurante.iniciarPedido("Ana Lopez", "Calle 123");
			Pedido pedido = restaurante.getPedidoEnCurso();
			pedido.agregarProducto(new ProductoMenu("corral", 14000));
			factura = new File("./facturas/factura_" + pedido.getIdPedido() + ".txt");

			restaurante.cerrarYGuardarPedido();

			assertNull(restaurante.getPedidoEnCurso(), "Ya no debe haber pedido en curso");
			assertEquals(1, restaurante.getPedidos().size(), "El pedido terminado debe quedar en el historial");
			assertEquals(pedido, restaurante.getPedidos().get(0), "El pedido guardado no es el correcto");
			assertTrue(factura.exists(), "Debió crear el archivo de la factura");
		}
		catch (Exception e) {
			fail("No debió lanzar excepción: " + e.getMessage());
		}
		factura.delete();
	}

	@Test
	public void cerrarYGuardarSinPedidoTest() {
		setup();
		try {
			restaurante.cerrarYGuardarPedido();
			fail("Debió lanzar NoHayPedidoEnCursoException");
		}
		catch (NoHayPedidoEnCursoException e) {
			assertTrue(restaurante.getPedidos().isEmpty(), "No debió guardar ningún pedido");
		}
		catch (IOException e) {
			fail("Debió lanzar NoHayPedidoEnCursoException, no IOException");
		}
	}

	@Test
	public void cargarInformacionRestauranteTest() {
		setup();
		try {
			restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
		}
		catch (Exception e) {
			fail("No debió lanzar excepción: " + e.getMessage());
		}
		assertEquals(15, restaurante.getIngredientes().size(), "Debió cargar 15 ingredientes");
		assertEquals("lechuga", restaurante.getIngredientes().get(0).getNombre(), "El primer ingrediente no es correcto");
		assertEquals(1000, restaurante.getIngredientes().get(0).getCostoAdicional(), "El costo no es correcto");

		assertEquals(22, restaurante.getMenuBase().size(), "Debió cargar 22 productos");
		assertEquals("corral", restaurante.getMenuBase().get(0).getNombre(), "El primer producto no es correcto");
		assertEquals(14000, restaurante.getMenuBase().get(0).getPrecio(), "El precio no es correcto");

		assertEquals(4, restaurante.getMenuCombos().size(), "Debió cargar 4 combos");
		assertEquals("combo corral", restaurante.getMenuCombos().get(0).getNombre(), "El primer combo no es correcto");
		assertEquals(22050, restaurante.getMenuCombos().get(0).getPrecio(), "El precio del combo no es correcto");
	}

	@Test
	public void cargarIngredienteRepetidoTest() {
		setup();
		try {
			restaurante.cargarInformacionRestaurante(new File("./data/ingredientesRepetidos.txt"), menu, combos);
			fail("Debió lanzar IngredienteRepetidoException");
		}
		catch (IngredienteRepetidoException e) {
			assertTrue(e.getMessage().contains("tomate"), "El mensaje debe incluir el ingrediente repetido");
		}
		catch (Exception e) {
			fail("Lanzó una exception diferente: " + e.getMessage());
		}
	}

	@Test
	public void cargarProductoRepetidoTest() {
		setup();
		try {
			restaurante.cargarInformacionRestaurante(ingredientes, new File("./data/menuRepetido.txt"), combos);
			fail("Debió lanzar ProductoRepetidoException");
		}
		catch (ProductoRepetidoException e) {
			assertTrue(e.getMessage().contains("corral"), "El mensaje debe incluir el producto repetido");
		}
		catch (Exception e) {
			fail("Lanzó una exception diferente: " + e.getMessage());
		}
	}

	@Test
	public void cargarComboRepetidoTest() {
		setup();
		try {
			restaurante.cargarInformacionRestaurante(ingredientes, menu, new File("./data/combosRepetidos.txt"));
			fail("Debió lanzar ProductoRepetidoException");
		}
		catch (ProductoRepetidoException e) {
			assertTrue(e.getMessage().contains("combo corral"), "El mensaje debe incluir el combo repetido");
		}
		catch (Exception e) {
			fail("Lanzó una exception diferente: " + e.getMessage());
		}
	}

	@Test
	public void cargarComboConProductoFaltanteTest() {
		setup();
		try {
			restaurante.cargarInformacionRestaurante(ingredientes, menu, new File("./data/combosFaltantes.txt"));
			fail("Debió lanzar ProductoFaltanteException");
		}
		catch (ProductoFaltanteException e) {
			assertTrue(e.getMessage().contains("hamburguesa inexistente"), "El mensaje debe incluir el producto faltante");
		}
		catch (Exception e) {
			fail("Lanzó una exception diferente: " + e.getMessage());
		}
	}

	@Test
	public void cargarArchivoInexistenteTest() {
		setup();
		try {
			restaurante.cargarInformacionRestaurante(new File("./data/no_existe.txt"), menu, combos);
			fail("Debió lanzar IOException");
		}
		catch (IOException e) {
			assertTrue(restaurante.getIngredientes().isEmpty(), "No debió cargar ingredientes");
		}
		catch (HamburguesaException e) {
			fail("Debió lanzar IOException, no una excepción de la aplicación");
		}
	}

}
