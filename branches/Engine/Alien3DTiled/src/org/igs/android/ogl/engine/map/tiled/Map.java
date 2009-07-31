package org.igs.android.ogl.engine.map.tiled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.igs.android.ogl.engine.AndromedaException;
import org.igs.android.ogl.engine.Renderer;
import org.igs.android.ogl.engine.math.Point3f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
*
* @author Elizeu Nogueira da Rosa Jr.
* @version 0.1
* @date 21.02.2008
* 
*/
public abstract class Map {

    public static final int TYPE_ORTHOGONAL = 5001;
    public static final int TYPE_ISOMETRIC = 5002;
    
    /** map design version */
    private String version;
    
    /** static Map.TYPE_ORTHOGONAL or Map.TYPE_ISOMETRIC*/
    private int orientation; 
    
    /** width in tiles */
    private int width;
    
    /** heigth in tiles */
    private int height;
    
    /** width in pixels */
    private int tileWidth;
    
    /** height in pixels */
    private int tileHeight;

    /** name of map */
    
    /** Map file name */
    private String fileName;
    
    /** List of tiles */
    private List<TileSet> tileSetList;
    
    /** list of layers */
    private List<Layer> layerList;

    /** list of properties */
    private Hashtable<String, Object> properties;
    
    private Renderer renderer;
    
    /**
     * 
     * @param name path to map file
     */
    public Map(Renderer renderer, String fileName) {
        this.fileName = fileName;
        this.renderer = renderer;
    }

    public final String getVersion() {
        return version;
    }

    private void setVersion(String version) {
        this.version = version;
    }

    public int getOrientation() {
        return orientation;
    }

    private void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getWidth() {
        return width;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public final int getHeight() {
        return height;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    private void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public final int getTileHeight() {
        return tileHeight;
    }

    private void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public final String getFileName() {
        return fileName;
    }

    public final List<TileSet> getTileSetList() {
        if (this.tileSetList == null) {
            this.setTileSetList(new ArrayList<TileSet>(1));
        }
        return tileSetList;
    }

    private void setTileSetList(List<TileSet> tileSetList) {
        this.tileSetList = tileSetList;
    }
    
    private void addTileSet(TileSet tileSet) {
        this.getTileSetList().add(tileSet);
    }
    
    public final TileSet findTileSet(String name) {
        for (TileSet ts : this.getTileSetList()) {
            if (ts.getName().equals(name)) {
                return ts;
            }
        }
        return null;
    }

    public final List<Layer> getLayerList() {
        if (this.layerList == null) {
            this.setLayerList(new ArrayList<Layer>(1));
        }
        return layerList;
    }

    private void setLayerList(List<Layer> layerList) {
        this.layerList = layerList;
    }
    
    private void addLayer(Layer layer) {
        this.getLayerList().add(layer);
    }
    
    public final Hashtable<String, Object> getProperties() {
        return properties;
    }

    private void setProperties(Hashtable<String, Object> properties) {
        this.properties = properties;
    }

    /**
     * Initialize XML map
     */
    public final void initialize() throws AndromedaException {
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = db.parse(this.renderer.getContext().getAssets().open(this.getFileName()));
            this.buildMap(doc.getDocumentElement());
        } catch (SAXException ex) {
            //Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SAXException: " + ex.getMessage());
        } catch (IOException ex) {
            //Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("IOException: " + ex.getMessage());
        } catch (ParserConfigurationException ex) {
            //Logger.getLogger(Map.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ParserConfigurationException: " + ex.getMessage());
        }
    }
    
    /**
     * Create a map
     * 
     * @param element XML Tag element &lt;map&gt;
     */
    private void buildMap(Element element) throws AndromedaException {
        this.setVersion(element.getAttribute(""));
        this.setOrientation(element.getAttribute("orthogonal").equals("orthogonal") ? Map.TYPE_ORTHOGONAL : Map.TYPE_ISOMETRIC);
        this.setWidth(Integer.parseInt(element.getAttribute("width")));
        this.setHeight(Integer.parseInt(element.getAttribute("height")));
        this.setTileWidth(Integer.parseInt(element.getAttribute("tilewidth")));
        this.setTileHeight(Integer.parseInt(element.getAttribute("tileheight")));
        NodeList mapNodeList = element.getChildNodes();
        for (int i = 0; i < mapNodeList.getLength(); i++) {
            Node mapNode = mapNodeList.item(i);
            if ((mapNode != null) && (mapNode.getNodeType() == Node.ELEMENT_NODE)) {
                if (mapNode.getNodeName().equals("properties")) {
                    this.setProperties(this.buildProperties((Element)mapNode));
                }
                else if (mapNode.getNodeName().equals("tileset")) {
                    this.addTileSet(this.buildTileSet((Element)mapNode));
                }
                else if (mapNode.getNodeName().equals("layer")) {
                    this.addLayer(this.buildLayer((Element)mapNode));
                }
            }
        }
    }
    
    /**
     * Create a Hashtable &lt;String, String&gt; for attributes 
     * name, value of tag &lt;property&gt;
     * 
     * @param element XML Tag element &lt;properties&gt;
     * @return Hashtable contains &lt;name, value&gt; of tag &lt;property name="name" value="value" /&gt;
     */
    private Hashtable<String, Object> buildProperties(Element element) {
        Hashtable<String, Object> resultHash = new Hashtable<String, Object>(1);
        NodeList nl = element.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node node = nl.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elementNode = (Element)node;
                resultHash.put(elementNode.getAttribute("name"), elementNode.getAttribute("value"));
            }
        }
        return resultHash;
    }

    public void drawTile(int index, Point3f position) {
    	for (TileSet tileSet : this.getTileSetList()) {
    		if (tileSet.getFirstGID() <= index && tileSet.getLastGID() >= index) {
    			tileSet.renderTile(index, position);
    			break;
    		}
    	}
    }
    
    /**
     * Create a TileSet for attributes 
     * 
     * @param element XML Tag element &lt;tileset&gt;
     * @return TileSet TileSet class
     */
    private TileSet buildTileSet(Element element) throws AndromedaException {
        TileSet tileSet = new TileSet(this.renderer, this);
        tileSet.setName(element.getAttribute("name"));
        tileSet.setFirstGID(Integer.parseInt(element.getAttribute("firstgid")));
        tileSet.setTileWidth(Integer.parseInt(element.getAttribute("tilewidth")));
        tileSet.setTileHeight(Integer.parseInt(element.getAttribute("tileheight")));
        Element imageNode = (Element)element.getElementsByTagName("image").item(0);
        tileSet.setImageSource(imageNode.getAttribute("source"));
        tileSet.initTiles();
        //tileSet.setTransparency(imageNode.getAttribute("trans"));
        NodeList tileNodeList = element.getChildNodes();
        
        if (tileNodeList.getLength() > 0) {
            for (int i = 0; i < tileNodeList.getLength(); i++) {
                Node node = tileNodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getNodeName().equals("tile")) {
                        NodeList propNodeList = node.getChildNodes();
                        for (int j = 0; j < propNodeList.getLength(); j++) {
                            Node propNode = propNodeList.item(j);
                            if (propNode.getNodeType() == Node.ELEMENT_NODE) {
                            	tileSet.setTileProperties(new Integer(((Element)node).getAttribute("id")), this.buildProperties((Element)propNode));
                            }
                        }
                    }
                }
            }
        }
        return tileSet;
    }
    
    /**
     * Create a Layer for attributes 
     * 
     * @param element XML Tag element &lt;layer&lt;
     * @return Layer Layer class
     */
    private Layer buildLayer(Element element) {
        Layer layer = new Layer(this);
        layer.setName(element.getAttribute("name"));
        layer.setWidth(Integer.parseInt(element.getAttribute("width")));
        layer.setHeight(Integer.parseInt(element.getAttribute("height")));
        NodeList tileNodeList = element.getChildNodes();
        List<Tile> tileList = new ArrayList<Tile>(layer.getWidth() * layer.getHeight());
        
        
        float actualTileX = 0;
        float actualTileY = 0;
        
        int columnIndex = 0;
        
        for (int i = 0; i < tileNodeList.getLength(); i++) {
            Node node = tileNodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (node.getNodeName().equals("data")) {
                    NodeList dataChilds = node.getChildNodes();
                    for (int j = 0; j < dataChilds.getLength(); j++) {
                        Node nodeChild = dataChilds.item(j);
                        if (nodeChild.getNodeType() == Node.ELEMENT_NODE) {
                            Element elementChild = (Element)nodeChild;
                            int gid = Integer.parseInt(elementChild.getAttribute("gid"));
                            if (gid > 0) {
                            	tileList.add(new Tile(gid, new Point3f(actualTileX, actualTileY, 0f)));
                            }
                            columnIndex++;
                            if (columnIndex == layer.getWidth()) {
                            	columnIndex = 0;
                            	actualTileX = 0f;
                            	actualTileY -= this.getTileHeight() / this.getMapProportion();
                            } else {
                            	actualTileX += this.getTileWidth() / this.getMapProportion();
                            }
                        }
                    }
                }
            }
        }
        layer.setTileList(tileList);
        return layer;
    }
     
    private float mapX;
    private float mapY;
    
    public void moveMapX(float value) {
    	mapX += value;
    }

    public void moveMapY(float value) {
    	mapY += value;
    }
    
    public void render(float delta) {
    	for (Layer layer  : this.getLayerList()) {
    		layer.render(delta, mapX, mapY);
    	}
    }
    
    public abstract float getMapProportion();

    public abstract float getXProportion();

    public abstract float getYProportion();
    
/*    
    private void renderIsometric(int x, int y, int cellX, int cellY, int width, int height) {
        for (Layer layer : this.getLayerList())
        //this.getLayerList().get(1).
        int tileCompensate = this.getTileWidth() / 2;
        int tileLeft = (this.getWidth() * tileCompensate) - tileCompensate;
                 
    }
*/
	
}
