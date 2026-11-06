package uniandes.dpoo.hamburguesas.tests;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Test;

public class PedidoTest {

	
	private Pedido pedido;
	private ProductoMenu corral;
	private ProductoMenu papas;

	
	public void setup() {
		pedido = new Pedido("Ana Lopez", "Calle 123");
		corral = new ProductoMenu("corral", 14000);
		papas = new ProductoMenu("papas medianas", 5500);
	}

	

	@Test
	public void getIdPedidoTest() {
		setup();
		Pedido siguiente = new Pedido("Luis Gomez", "Carrera 7");
		assertEquals(pedido.getIdPedido() + 1, siguiente.getIdPedido(), "Cada pedido nuevo debe tener el id siguiente");
	}

	@Test
	public void getNombreClienteTest() {
		setup();
		assertEquals("Ana Lopez", pedido.getNombreCliente(), "El nombre del cliente no es correcto");
	}

	@Test
	public void getPrecioTotalPedidoTest() {
		setup();
		assertEquals(0, pedido.getPrecioTotalPedido(), "Un pedido vacío debe valer 0");

		
		pedido.agregarProducto(corral);
		assertEquals(16660, pedido.getPrecioTotalPedido(), "El total con un producto no es correcto");

		
		pedido.agregarProducto(papas);
		assertEquals(23205, pedido.getPrecioTotalPedido(), "El total con dos productos no es correcto");
	}

	@Test
	public void generarTextoFacturaTest() {
		setup();
		pedido.agregarProducto(corral);
		pedido.agregarProducto(papas);
		String esperado = "Cliente: Ana Lopez\n"
				+ "Dirección: Calle 123\n"
				+ "----------------\n"
				+ "corral\n" + "            14000\n"
				+ "papas medianas\n" + "            5500\n"
				+ "----------------\n"
				+ "Precio Neto:  19500\n"
				+ "IVA:          3705\n"
				+ "Precio Total: 23205\n";
		assertEquals(esperado, pedido.generarTextoFactura(), "El texto de la factura no es correcto");
	}

	@Test
	public void generarTextoFacturaVaciaTest() {
		setup();
		String esperado = "Cliente: Ana Lopez\n"
				+ "Dirección: Calle 123\n"
				+ "----------------\n"
				+ "----------------\n"
				+ "Precio Neto:  0\n"
				+ "IVA:          0\n"
				+ "Precio Total: 0\n";
		assertEquals(esperado, pedido.generarTextoFactura(), "La factura de un pedido vacío no es correcta");
	}

	@Test
	public void guardarFacturaTest() {
		setup();
		pedido.agregarProducto(corral);
		File archivo = new File("./factura_prueba.txt");
		try {
			pedido.guardarFactura(archivo);
			String contenido = new String(Files.readAllBytes(archivo.toPath()));
			assertEquals(pedido.generarTextoFactura(), contenido, "El archivo no tiene la factura del pedido");
		}
		catch (IOException e) {
			fail("No debió lanzar exception");
		}
		archivo.delete();
	}

	@Test
	public void guardarFacturaEnCarpetaInexistenteTest() {
		setup();
		File archivo = new File("./carpeta_que_no_existe/factura.txt");
		try {
			pedido.guardarFactura(archivo);
			fail("Debió lanzar FileNotFoundException");
		}
		catch (FileNotFoundException e) {
			assertTrue(!archivo.exists(), "No debió crear el archivo");
		}
	}

}
