package konkon.controller;

import konkon.model.Item;
import konkon.model.Product;
import konkon.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes("product")
public class ProductController {
  @Autowired
  private ProductService productService;
  @GetMapping("/list")
  public String showList(Model model){
    List<Product> products = productService.findAll();
    model.addAttribute("products", products);
    return "list";
  }
  @GetMapping("/view/{id}")
  public String showViewForm(@PathVariable Long id, Model model){
    Product product =  productService.findById(id);
    model.addAttribute("product", product);
    return "view";
  }
  @ModelAttribute("product")
  public Product setUpMyProduct(){
    return new Product();
  }
  @PostMapping("/add-to-cart")
  public String addToCart(@ModelAttribute Product product, Model model, HttpSession session){
   if(session.getAttribute("cart") == null){
     List<Item> cart = new ArrayList<Item>();
     cart.add(new Item(product, 1));
     session.setAttribute("cart", cart);
   }else{
     List<Item> cart = (List<Item>) session.getAttribute("cart");
     int index = isExisting(product.getId(),session);
     if(index == -1)
       cart.add(new Item(product,1));
     else{
       int quantiyty = cart.get(index).getQuantity()+1;
       cart.get(index).setQuantity(quantiyty);
     }
     session.setAttribute("cart",cart);
   }
   return "cart";
  }

  private int isExisting(long id, HttpSession session) {
    List<Item> cart = (List<Item>) session.getAttribute("cart");

    for (int i = 0; i < cart.size(); i++)

      if (cart.get(i).getProduct().getId() == id)
        return i;

    return -1;
  }
  @GetMapping("/cart")
  public String showCartForm(){
    return "cart";
  }

  @GetMapping("/edit-quantity/{id}")
  public ModelAndView editQuantityForm(@PathVariable long id, HttpSession session){
    ModelAndView modelAndView = new ModelAndView("/edit");
    List<Item> cart = (List<Item>) session.getAttribute("cart");
    int index = isExisting(id, session);
    Item item = cart.get(index);
    modelAndView.addObject("item", item);
    return modelAndView;
  }
  @PostMapping("/edit-quantity/{id}")
  public String editQuantity(@ModelAttribute Item item, @PathVariable long id,@RequestParam("quantity") int quantity, HttpSession session){
    List<Item> cart = (List<Item>) session.getAttribute("cart");
    int index = isExisting(id, session);
    cart.get(index).setQuantity(quantity);
    session.setAttribute("cart", cart);
    return "redirect:/cart";
  }

  @GetMapping("/delete/{id}")
  public ModelAndView deleteForm(@PathVariable long id, HttpSession session){
    ModelAndView modelAndView = new ModelAndView("/delete");
    List<Item> cart = (List<Item>) session.getAttribute("cart");
    int index = isExisting(id, session);
    Item item = cart.get(index);
    modelAndView.addObject("item", item);
    return modelAndView;
  }

  @PostMapping("/delete/{id}")
  public String delete(@PathVariable long id, HttpSession session){
    List<Item> cart = (List<Item>) session.getAttribute("cart");
    int index = isExisting(id, session);
    cart.remove(index);
    session.setAttribute("cart", cart);
    return "redirect:/cart";
  }

  @GetMapping("/deleteAll")
  public String deleteAll(HttpSession session){
    session.setAttribute("cart",null);
    return "redirect:/cart";
  }


}
