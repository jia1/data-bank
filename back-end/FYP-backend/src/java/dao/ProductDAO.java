/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import database.ConnectionManager;
import entity.Bedding;
import entity.BeddingSize;
import entity.Colour;
import entity.Pattern;
import entity.Fabric;
import entity.Image;
import entity.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Huiyan
 */
public class ProductDAO {

    public ArrayList<Bedding> getFilteredBeddingPatterns(int collectionId, int fabricId, int colourId, String sortPrice, String search) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Bedding> beddings = new ArrayList<>();

        String sql = "SELECT pro.*, pat.PATTERN_NAME, pat.COLLECTION_ID FROM PRODUCT pro LEFT OUTER JOIN PATTERN pat ON pro.PATTERN_ID = pat.PATTERN_ID WHERE pro.DELETED = 'N'";

        if(collectionId > 0){
            
            sql = sql + " AND pat.COLLECTION_ID = " + collectionId;
            
        }
        
        if(fabricId > 0){
            
            sql = sql + " AND pro.FABRIC_ID = " + fabricId;
            
        }
        
        if(colourId > 0){
            
            sql = sql + " AND pro.COLOUR_ID = " + colourId;
            
        }
        
        if(!search.equals("undefined")){
            
            sql = sql + " AND PATTERN_NAME LIKE \'%" + search + "%\'";
            
        }
        
        sql = sql + " GROUP BY pat.PATTERN_ID";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);

            rs = stmt.executeQuery();

            while (rs.next()) {

                int productId = rs.getInt("product_id");
                int patternId = rs.getInt("pattern_id");
                int colourID = rs.getInt("colour_id");
                String sizeName = "Single";
                int fabricID = rs.getInt("fabric_id");

                PatternDAO dd = new PatternDAO();
                ColourDAO cd = new ColourDAO();
                ImageDAO id = new ImageDAO();
                BeddingSizeDAO bzd = new BeddingSizeDAO();
                FabricDAO fd = new FabricDAO();

                Pattern d = dd.getPatternById(patternId);
                Colour c = cd.getColourById(colourID);
                BeddingSize bs = bzd.getBeddingSizeByName(sizeName);
                Fabric f = fd.getFabricById(fabricID);
                Image[] images = id.getAllImagesByProductId(productId);

                Bedding bedding = new Bedding(bs, productId, "Bedding", d, c, f, images);
                beddings.add(bedding);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return beddings;
    }
    
    //Create 1 Product
    public void addProduct(Product p) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Image[] images = p.getImages();
        int nextProductId = getNextProductId();

        String sql = "INSERT INTO PRODUCT (PRODUCT_ID, PRODUCT_TYPE, PATTERN_ID, COLOUR_ID, FABRIC_ID, DELETED) VALUES (?,?,?,?,?,?)";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, nextProductId);
            stmt.setString(2, p.getProductType());
            stmt.setInt(3, p.getPattern().getPatternId());
            stmt.setInt(4, p.getColour().getColourId());
            stmt.setInt(5, p.getFabric().getFabricId());
            stmt.setString(6, "N");
            stmt.executeUpdate();
            
            for(Image i : images){
                
                ImageDAO iDao = new ImageDAO();
                iDao.addImage(nextProductId, i);
                
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

    }

    public int getNextProductId() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int nextProductId = 0;

        String sql = "SELECT MAX(PRODUCT_ID) AS MAX FROM PRODUCT";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            while (rs.next()) {

                nextProductId = rs.getInt("MAX") + 1;

            }

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

        return nextProductId;

    }

    //Update
    public void updateProduct(Product p) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE PRODUCT SET PRODUCT_TYPE = ?, DESIGN_ID = ?, COLOUR_ID = ? WHERE PRODUCT_ID = ?";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, p.getProductType());
            stmt.setInt(2, p.getPattern().getPatternId());
            stmt.setInt(3, p.getColour().getColourId());
            stmt.setInt(4, p.getProductId());
            stmt.executeUpdate();

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

    }

    //Soft Delete
    public void deleteProductById(int productId) throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        String sql = "UPDATE PRODUCT SET DELETED = 'Y' WHERE PRODUCT_ID = ?";

        try {

            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            stmt.executeUpdate();

        } finally {

            ConnectionManager.close(conn, stmt, rs);

        }

    }

    public ArrayList<Bedding> getBeddingPatterns() throws SQLException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Bedding> beddings = new ArrayList<>();

        String sql = "SELECT  * FROM Pattern P, PRODUCT PR, BEDDING B WHERE P.PATTERN_ID=PR.PATTERN_ID AND PR.PRODUCT_ID=B.PRODUCT_ID AND PR.DELETED=? group by P.PATTERN_NAME";

        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "N");

            rs = stmt.executeQuery();

            while (rs.next()) {

                int productId = rs.getInt("product_id");
                int patternId = rs.getInt("pattern_id");
                int colourId = rs.getInt("colour_id");
                String sizeName = rs.getString("size_name");
                int fabricId = rs.getInt("fabric_id");

                PatternDAO dd = new PatternDAO();
                ColourDAO cd = new ColourDAO();
                ImageDAO id = new ImageDAO();
                BeddingSizeDAO bzd = new BeddingSizeDAO();
                FabricDAO fd = new FabricDAO();

                Pattern d = dd.getPatternById(patternId);
                Colour c = cd.getColourById(colourId);
                BeddingSize bs = bzd.getBeddingSizeByName(sizeName);
                Fabric f = fd.getFabricById(fabricId);
                Image[] images = id.getAllImagesByProductId(productId);

                Bedding bedding = new Bedding(bs, productId, "Bedding", d, c, f, images);
                beddings.add(bedding);
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return beddings;
    }

    public Product getProductById(int productId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        PreparedStatement stmt2 = null;
        ResultSet rs2 = null;
        Product product = null;

        String sql = "SELECT * FROM PRODUCT WHERE PRODUCT_ID=? ";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, productId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                int patternId = rs.getInt("PATTERN_ID");
                PatternDAO dd = new PatternDAO();
                ColourDAO cd = new ColourDAO();
                FabricDAO fd = new FabricDAO();
                ImageDAO id = new ImageDAO();

                int colourId = rs.getInt("COLOUR_ID");
                int fabricId = rs.getInt("FABRIC_ID");
                Colour c = cd.getColourById(colourId);
                Fabric f = fd.getFabricById(fabricId);
                Pattern d = dd.getPatternById(patternId);
                Image[] images = id.getAllImagesByProductId(productId);
                String productType = rs.getString("PRODUCT_TYPE");

                if (productType.equals("Bedding")) {
                    String sql2 = "SELECT * FROM BEDDING WHERE PRODUCT_ID=?";
                    stmt2 = conn.prepareStatement(sql2);
                    stmt2.setInt(1, productId);
                    rs2 = stmt2.executeQuery();
                    while (rs2.next()) {

                        String sizeName = rs2.getString("SIZE_NAME");
                        BeddingSizeDAO bzd = new BeddingSizeDAO();

                        BeddingSize bs = bzd.getBeddingSizeByName(sizeName);
                        product = new Bedding(bs, productId, "Bedding", d, c, f, images);
                    }

                } else {
                    product = new Product(productId, productType, d, c, f, images);
                }
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return product;
    }

    public ArrayList<Bedding> getAllBeddings() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Bedding> beddings = new ArrayList<>();
        String sql = "SELECT P.*, B.SIZE_NAME FROM PRODUCT P LEFT OUTER JOIN BEDDING B ON B.PRODUCT_ID=P.PRODUCT_ID WHERE P.PRODUCT_TYPE=? AND DELETED = 'N'";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "Bedding");
            rs = stmt.executeQuery();
            while (rs.next()) {

                int productId = rs.getInt("product_id");
                int patternId = rs.getInt("pattern_id");
                int colourId = rs.getInt("colour_id");
                String sizeName = rs.getString("size_name");
                int fabricId = rs.getInt("fabric_id");

                PatternDAO dd = new PatternDAO();
                ColourDAO cd = new ColourDAO();
                ImageDAO id = new ImageDAO();
                BeddingSizeDAO bzd = new BeddingSizeDAO();
                FabricDAO fd = new FabricDAO();

                Pattern d = dd.getPatternById(patternId);
                Colour c = cd.getColourById(colourId);
                BeddingSize bs = bzd.getBeddingSizeByName(sizeName);
                Fabric f = fd.getFabricById(fabricId);
                Image[] images = id.getAllImagesByProductId(productId);

                Bedding bedding = new Bedding(bs, productId, "Bedding", d, c, f, images);
                beddings.add(bedding);

            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return beddings;

    }
//

//
    public Product getProductByPatternFabricColor(int patternId, int fabricId, int colourId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Product p = null;

        String sql = "select product_id from product where fabric_id=? and pattern_id=? and colour_id = ? and deleted=? ";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, fabricId);
            stmt.setInt(2, patternId);
            stmt.setInt(3, colourId);
            stmt.setString(4, "N");
            
            rs = stmt.executeQuery();

            while (rs.next()) {

                int productId = rs.getInt("product_id");
                p =  getProductById(productId);

            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return p;
    }
    
    public double getLowestCombinationPriceByPatternId(int patternId) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        double lowestPrice=9999999999999999999999.9;
        BeddingSizeDAO beddingSizeDao= new BeddingSizeDAO();

        String sql = "select * from product p, pattern pa, fabric f, bedding b where p.pattern_id=pa.pattern_id and p.product_id=b.product_id and p.fabric_id=f.fabric_id and p.pattern_id=?";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, patternId);   
            rs = stmt.executeQuery();

            while (rs.next()) {
                double patternPrice = rs.getDouble("pattern_price");
                double fabricPrice= rs.getDouble("fabric_price");
                double sizePrice= beddingSizeDao.getLowestSizePrice();
                double totalPrice= patternPrice+fabricPrice+sizePrice;
                if(totalPrice<=lowestPrice){
                    lowestPrice=totalPrice;
                }
                
            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }
        return lowestPrice;
    }
    
//
//    public static Product[] getfilteredProducts(String collectionId, String fabricId, String colourId, String sortPrice) throws SQLException {
//
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        Product product = null;
//        ArrayList<Product> productArrayList = new ArrayList();
//        System.out.println(collectionId + fabricId + colourId + sortPrice);
//        String sql = "SELECT p1.*, p2.COLLECTION_ID FROM PRODUCT p1 LEFT OUTER JOIN PATTERN p2 ON p1.PATTERN_ID = p2.PATTERN_ID WHERE ";
//
//        if (collectionId != null) {
//
//            sql += "COLLECTION_ID = ? AND";
//
//        }
//
//        if (fabricId != null) {
//
//            sql += " FABRIC_ID = ? AND";
//
//        }
//
//        if (colourId != null) {
//
//            sql += " COLOUR_ID = ? AND";
//
//        }
//
//        sql = sql.substring(0, sql.length() - 3);
//        sql += " GROUP BY (p1.pattern_id) ORDER BY p1.COLOUR_PRICE";
//
//        if (sortPrice.equals("desc")) {
//
//            sql += " DESC";
//
//        }
//        
//        System.out.println(sql);
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//
//            if (collectionId != null) {
//
//                stmt.setString(1, collectionId);
//
//                if (fabricId != null) {
//
//                    stmt.setString(2, fabricId);
//
//                    if (colourId != null) {
//
//                        stmt.setString(3, colourId);
//
//                    }
//
//                }else if(colourId != null){
//                    
//                    stmt.setString(2, colourId);
//                      
//                }
//
//            } else if (fabricId != null) {
//
//                stmt.setString(1, fabricId);
//
//                if (colourId != null) {
//
//                    stmt.setString(2, colourId);
//
//                }
//
//            } else if (colourId != null) {
//
//                stmt.setString(1, colourId);
//
//            }
//
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//
//                String patternID = rs.getString("PATTERN_ID");
//                String sku = rs.getString("SKU");
//                String colourID = rs.getString("COLOUR_ID");
//                String fabricID = rs.getString("FABRIC_ID");
//                double colorPrice = rs.getDouble("COLOUR_PRICE");
//                String imageUrl = rs.getString("IMAGE_URL");
//
//                Fabric f = FabricDAO.getFabricById(fabricID);
//                Colour c = ColourDAO.getColorById(colourID);
//                Pattern p = PatternDAO.retrievePatternById(patternID);
//                Collection col = p.getCollection();
//                ArrayList<String> tags = PatternDAO.getTagsByPatternId(patternID);
//
//                product = new Product(sku, patternID, p.getPatternName(), p.getPatternPrice(), f.getFabricID(), f.getFabricName(), f.getFabricPrice(), c.getColourID(), c.getColourName(), colorPrice, col.getCollectionID(), col.getCollectionName(), imageUrl, tags);
//
//                productArrayList.add(product);
//            }
//
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//        return productArrayList.toArray(new Product[productArrayList.size()]);
//    }
//    
//    public static Product[] getSearchProducts(String search) throws SQLException {
//        System.out.print(search);
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        Product product = null;
//        ArrayList<Product> productArrayList = new ArrayList();
//
//        String sql = "SELECT p1.* FROM PRODUCT p1 LEFT OUTER JOIN PATTERN p2 ON p1.PATTERN_ID = p2.PATTERN_ID WHERE PATTERN_NAME LIKE '%";
//        sql = sql + search + "%' GROUP BY (p1.pattern_id)";
//        System.out.println(sql);
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//
//                String patternID = rs.getString("PATTERN_ID");
//                String sku = rs.getString("SKU");
//                String colourID = rs.getString("COLOUR_ID");
//                String fabricID = rs.getString("FABRIC_ID");
//                double colorPrice = rs.getDouble("COLOUR_PRICE");
//                String imageUrl = rs.getString("IMAGE_URL");
//
//                Fabric f = FabricDAO.getFabricById(fabricID);
//                Colour c = ColourDAO.getColorById(colourID);
//                Pattern p = PatternDAO.retrievePatternById(patternID);
//                Collection col = p.getCollection();
//                ArrayList<String> tags = PatternDAO.getTagsByPatternId(patternID);
//
//                product = new Product(sku, patternID, p.getPatternName(), p.getPatternPrice(), f.getFabricID(), f.getFabricName(), f.getFabricPrice(), c.getColourID(), c.getColourName(), colorPrice, col.getCollectionID(), col.getCollectionName(), imageUrl, tags);
//
//                productArrayList.add(product);
//            }
//
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//        return productArrayList.toArray(new Product[productArrayList.size()]);
//    }
//    
//    public static void updatePatternFabric(String patternID, String fabricID) {
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        String sql = "INSERT IGNORE INTO pattern_fabric (PATTERN_ID, FABRIC_ID) VALUES (?, ?)";
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//            stmt.setString(1, patternID);
//            stmt.setString(2, fabricID);
//           
//            stmt.executeUpdate();
//            
//        } catch (SQLException ex) {
//            handleSQLException(ex, sql);
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//    }
//    
//    private static void handleSQLException(SQLException ex, String sql, String... parameters) {
//        String msg = "Unable to access data; SQL=" + sql + "\n";
//        for (String parameter : parameters) {
//            msg += "," + parameter;
//        }
//        Logger.getLogger(CustomerDAO.class.getName()).log(Level.SEVERE, msg, ex);
//    }
//    
//    public static Product[] getfilteredProductList(String collectionId, String fabricId, String colourId, String sortPrice, String search) throws SQLException {
//
//        Connection conn = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        Product product = null;
//        ArrayList<Product> productArrayList = new ArrayList();
//
//        String sql = "SELECT p1.*, p2.COLLECTION_ID FROM PRODUCT p1 LEFT OUTER JOIN PATTERN p2 ON p1.PATTERN_ID = p2.PATTERN_ID WHERE ";
//
//        
//        if (!collectionId.equals("undefined")) {
//
//            sql += "COLLECTION_ID = ? AND";
//
//        }
//
//        if (!fabricId.equals("undefined")) {
//
//            sql += " FABRIC_ID = ? AND";
//
//        }
//
//        if (!colourId.equals("undefined")) {
//
//            sql += " COLOUR_ID = ? AND";
//
//        }
//        
//        if (!search.equals("undefined")){
//            
//            System.out.println(search);
//            sql += " PATTERN_NAME LIKE '%" + search + "%' AND";
//            
//        }
//
//        sql = sql.substring(0, sql.length() - 3);
//        sql += " GROUP BY (p1.pattern_id) ORDER BY p1.COLOUR_PRICE";
//
//        if (sortPrice.equals("desc")) {
//
//            sql += " DESC";
//
//        }
//        
//        System.out.println(sql);
//
//        try {
//            conn = ConnectionManager.getConnection();
//            stmt = conn.prepareStatement(sql);
//
//            if (!collectionId.equals("undefined")) {
//
//                stmt.setString(1, collectionId);
//
//                if (!fabricId.equals("undefined")) {
//
//                    stmt.setString(2, fabricId);
//
//                    if (!colourId.equals("undefined")) {
//
//                        stmt.setString(3, colourId);
//
//                    }
//
//                }else if(!colourId.equals("undefined")){
//                    
//                    stmt.setString(2, colourId);
//                      
//                }
//
//            } else if (!fabricId.equals("undefined")) {
//
//                stmt.setString(1, fabricId);
//
//                if (!colourId.equals("undefined")) {
//
//                    stmt.setString(2, colourId);
//
//                }
//
//            } else if (!colourId.equals("undefined")) {
//
//                stmt.setString(1, colourId);
//
//            }
//
//            rs = stmt.executeQuery();
//
//            while (rs.next()) {
//
//                String patternID = rs.getString("PATTERN_ID");
//                String sku = rs.getString("SKU");
//                String colourID = rs.getString("COLOUR_ID");
//                String fabricID = rs.getString("FABRIC_ID");
//                double colorPrice = rs.getDouble("COLOUR_PRICE");
//                String imageUrl = rs.getString("IMAGE_URL");
//
//                Fabric f = FabricDAO.getFabricById(fabricID);
//                Colour c = ColourDAO.getColorById(colourID);
//                Pattern p = PatternDAO.retrievePatternById(patternID);
//                Collection col = p.getCollection();
//                ArrayList<String> tags = PatternDAO.getTagsByPatternId(patternID);
//
//                product = new Product(sku, patternID, p.getPatternName(), p.getPatternPrice(), f.getFabricID(), f.getFabricName(), f.getFabricPrice(), c.getColourID(), c.getColourName(), colorPrice, col.getCollectionID(), col.getCollectionName(), imageUrl, tags);
//
//                productArrayList.add(product);
//            }
//
//        } finally {
//            ConnectionManager.close(conn, stmt, rs);
//        }
//        return productArrayList.toArray(new Product[productArrayList.size()]);
//    }
//    
    
    public ArrayList<Bedding> getAllBeddingsFilter() throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Bedding> beddings = new ArrayList<>();
        String sql = "SELECT P.*, B.SIZE_NAME FROM PRODUCT P LEFT OUTER JOIN BEDDING B ON B.PRODUCT_ID=P.PRODUCT_ID WHERE P.PRODUCT_TYPE=? AND DELETED = 'N'";
        try {
            conn = ConnectionManager.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "Bedding");
            rs = stmt.executeQuery();
            while (rs.next()) {

                int productId = rs.getInt("product_id");
                int patternId = rs.getInt("pattern_id");
                int colourId = rs.getInt("colour_id");
                String sizeName = rs.getString("size_name");
                int fabricId = rs.getInt("fabric_id");

                PatternDAO dd = new PatternDAO();
                ColourDAO cd = new ColourDAO();
                ImageDAO id = new ImageDAO();
                BeddingSizeDAO bzd = new BeddingSizeDAO();
                FabricDAO fd = new FabricDAO();

                Pattern d = dd.getPatternById(patternId);
                Colour c = cd.getColourById(colourId);
                BeddingSize bs = bzd.getBeddingSizeByName(sizeName);
                Fabric f = fd.getFabricById(fabricId);
                Image[] images = id.getAllImagesByProductId(productId);

                Bedding bedding = new Bedding(bs, productId, "Bedding", d, c, f, images);
                beddings.add(bedding);

            }

        } finally {
            ConnectionManager.close(conn, stmt, rs);
        }

        return beddings;

    }
}
