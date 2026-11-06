package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;


public class ProductoMenuTest {

	
	private ProductoMenu producto;

	
	public void setup() {
		producto = new ProductoMenu("HamburguesaSencilla", 14000);
	}

	

	@Test
	public void getNombreTest() {
		setup();
		assertEquals("HamburguesaSencilla", producto.getNombre(), "El nombre no es correcto");
	}

	@Test
	public void getPrecioTest() {
		setup();
		assertEquals(14000, producto.getPrecio(), "El precio no es correcto");
	}

	@Test
	public void generarTextoFacturaTest() {
		setup();
		String esperado = "HamburguesaSencilla\n" + "            14000\n";
		assertEquals(esperado, producto.generarTextoFactura(), "El texto de la factura no es correcto");
	}

}