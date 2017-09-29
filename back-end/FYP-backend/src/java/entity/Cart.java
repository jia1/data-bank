/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

/**
 *
 * @author Huiyan
 */
public class Cart {
    
    private int cartId;
    private double price;
    private CartItem[] cartItems;

    public Cart(int cartId, double price, CartItem[] cartItems) {
        this.cartId = cartId;
        this.price = price;
        this.cartItems = cartItems;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public CartItem[] getCartItems() {
        return cartItems;
    }

    public void setCartItem(CartItem[] cartItem) {
        this.cartItems = cartItem;
    }
   
}
