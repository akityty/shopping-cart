package konkon.service;

import konkon.model.Product;

import java.util.List;

public interface ProductService {
  List<Product> findAll();
  Product findById(Long id);
}
