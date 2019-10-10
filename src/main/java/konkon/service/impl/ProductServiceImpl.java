package konkon.service.impl;

import konkon.model.Product;
import konkon.repository.ProductRepository;
import konkon.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;

import javax.transaction.Transactional;
import java.util.List;
@Transactional
@PropertySource("classpath:global_config_app.properties")
public class ProductServiceImpl implements ProductService {
  @Autowired
  private ProductRepository productRepository;
  @Override
  public List<Product> findAll() {
    return (List<Product>) productRepository.findAll();
  }

  @Override
  public Product findById(Long id) {
    return productRepository.findOne(id);
  }

}
