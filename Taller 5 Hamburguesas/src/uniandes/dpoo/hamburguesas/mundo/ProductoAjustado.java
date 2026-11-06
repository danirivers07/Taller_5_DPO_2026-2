package uniandes.dpoo.hamburguesas.mundo;

import java.util.ArrayList;

/**
 * Un producto ajustado es un producto para el cual el cliente solicitó alguna modificación.
 */
public class ProductoAjustado implements Producto
{
    /**
     * El producto base que el cliente sobre el cual el cliente quiere hacer ajustes
     */
    private ProductoMenu productoBase;

    /**
     * La lista de ingrediente que el usuario quiere agregar. El mismo ingrediente puede aparecer varias veces.
     */
    private ArrayList<Ingrediente> agregados;

    /**
     * La lista de ingrediente que el usuario quiere eliminar.
     */
    private ArrayList<Ingrediente> eliminados;

    /**
     * Construye un nuevo producto ajustado a partir del producto base y sin modificaciones
     * @param productoBase El producto base que se va a ajustar
     */
    public ProductoAjustado( ProductoMenu productoBase )
    {
        this.productoBase = productoBase;
        agregados = new ArrayList<Ingrediente>( );
        eliminados = new ArrayList<Ingrediente>( );
    }

    @Override
    public String getNombre( )
    {
        return productoBase.getNombre( );
    }

    /**
     * Retorna el precio del producto ajustado, que debe ser igual al del producto base, sumándole el precio de los ingredientes adicionales.
     */
    @Override
    public int getPrecio( )
    {
    	int precio= productoBase.getPrecio();
    	for(Ingrediente ingrediente: agregados) {
    		
    		precio += ingrediente.getCostoAdicional();
    	}
    	
        return precio;
    }
    
    // Aquí hice el cambio porque el método estaba devolviendo solo 0, ahora devuelve
    // el precio base mas el costo adicional por ingrediente extra

    /**
     * Genera el texto que debe aparecer en la factura.
     * 
     * El texto incluye el producto base, los ingredientes adicionales con su costo, los ingredientes eliminados, y el precio total
     */
    
    public ProductoAjustado( ProductoMenu productoBase, ArrayList<Ingrediente> agregados, ArrayList<Ingrediente> eliminados )
    {
        this.productoBase = productoBase;
        this.agregados = new ArrayList<Ingrediente>( agregados );
        this.eliminados = new ArrayList<Ingrediente>( eliminados );
    }
    
    @Override
    public String generarTextoFactura( )
    {
        StringBuffer sb = new StringBuffer( );
        sb.append( productoBase.getNombre()+ "\n" );
        for( Ingrediente ing : agregados )
        {
            sb.append( "    +" + ing.getNombre( ));
            sb.append( "                " + ing.getCostoAdicional( ) + "\n");
        }
        for( Ingrediente ing : eliminados )
        {
            sb.append( "    -" + ing.getNombre( ) + "\n" );
        }

        sb.append( "            " + getPrecio( ) + "\n" );

        return sb.toString( );
    }

}
