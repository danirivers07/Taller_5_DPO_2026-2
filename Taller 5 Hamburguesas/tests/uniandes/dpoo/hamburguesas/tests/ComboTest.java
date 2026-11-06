package uniandes.dpoo.hamburguesas.tests;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;



public class ComboTest {

	
	private Combo combo;
	private ArrayList<ProductoMenu> items;

	
	public void setup() {
		items = new ArrayList<ProductoMenu>();
		items.add(new ProductoMenu("corral", 14000));
		items.add(new ProductoMenu("papas medianas", 5500));
		items.add(new ProductoMenu("gaseosa", 5000));
		combo = new Combo("combo corral", 0.1, items);
	}

	

	@Test
	public void getNombreTest() {
		setup();
		assertEquals("combo corral", combo.getNombre(), "El nombre no es correcto");
	}

	@Test
	public void getPrecioTest() {
		setup();
		// 24500 con 10% de descuento = 22050
		assertEquals(22050, combo.getPrecio(), "El precio con descuento no es correcto");

		// Sin descuento se debería pagar el valor completo
		Combo sinDescuento = new Combo("combo sin descuento", 0.0, items);
		assertEquals(24500, sinDescuento.getPrecio(), "Sin descuento debe valer la suma de los productos");

		// Con descuento del 50% = 0.5 se debería pagar la mitad
		Combo mitad = new Combo("combo mitad", 0.5, items);
		assertEquals(12250, mitad.getPrecio(), "Con 50% de descuento debe valer la mitad");
	}

	@Test
	public void getPrecioSiSeModificaMenuTest() {
		setup();
		
		items.add(new ProductoMenu("extra", 1000));
		assertEquals(22050, combo.getPrecio(), "El combo no debe cambiar si cambia la lista original");
	}

	@Test
	public void generarTextoFacturaTest() {
		setup();
		String esperado = "Combo combo corral\n" + " Descuento: 0.1\n" + "            22050\n";
		assertEquals(esperado, combo.generarTextoFactura(), "El texto de la factura no es correcto");
	}

}