package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;


public class ProductoAjustadoTest {

	
	private ProductoMenu base;
	private ProductoAjustado sinAjustes;
	private ProductoAjustado conAjustes;
	private Ingrediente queso;
	private Ingrediente tocineta;
	private Ingrediente tomate;


	public void setup() {
		base = new ProductoMenu("corral", 14000);
		queso = new Ingrediente("queso americano", 2500);
		tocineta = new Ingrediente("tocineta picada", 6000);
		tomate = new Ingrediente("tomate", 1000);

		
		sinAjustes = new ProductoAjustado(base);

		// Producto con ajustes: agrego queso (dos veces) y tocineta, y quito el tomate
		ArrayList<Ingrediente> agregados = new ArrayList<Ingrediente>();
		agregados.add(queso);
		agregados.add(tocineta);
		agregados.add(queso);
		ArrayList<Ingrediente> eliminados = new ArrayList<Ingrediente>();
		eliminados.add(tomate);
		conAjustes = new ProductoAjustado(base, agregados, eliminados);
	}



	@Test
	public void getNombreTest() {
		setup();
		assertEquals("corral", sinAjustes.getNombre(), "El nombre debe ser el del producto base");
		assertEquals("corral", conAjustes.getNombre(), "El nombre no cambia con los ajustes");
	}

	@Test
	public void getPrecioSinAjustesTest() {
		setup();
		assertEquals(14000, sinAjustes.getPrecio(), "Sin ajustes el precio es el del producto base");
	}

	@Test
	public void getPrecioConAgregadosTest() {
		setup();
		
		assertEquals(25000, conAjustes.getPrecio(), "Debe sumar todos los ingredientes agregados");
	}

	@Test
	public void getPrecioConEliminadosTest() {
		setup();
		ArrayList<Ingrediente> eliminados = new ArrayList<Ingrediente>();
		eliminados.add(tomate);
		ProductoAjustado soloEliminados = new ProductoAjustado(base, new ArrayList<Ingrediente>(), eliminados);
		assertEquals(14000, soloEliminados.getPrecio(), "Quitar ingredientes no reduce el precio");
	}

	@Test
	public void generarTextoFacturaSinAjustesTest() {
		setup();
		String esperado = "corral\n" + "            14000\n";
		assertEquals(esperado, sinAjustes.generarTextoFactura(), "El texto de la factura no es correcto");
	}

	@Test
	public void generarTextoFacturaConAjustesTest() {
		setup();
		String esperado = "corral\n"
				+ "    +queso americano" + "                2500\n"
				+ "    +tocineta picada" + "                6000\n"
				+ "    +queso americano" + "                2500\n"
				+ "    -tomate\n"
				+ "            25000\n";
		assertEquals(esperado, conAjustes.generarTextoFactura(), "El texto de la factura con ajustes no es correcto");
	}

}
