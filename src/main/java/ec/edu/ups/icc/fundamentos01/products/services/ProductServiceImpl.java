package ec.edu.ups.icc.fundamentos01.products.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ec.edu.ups.icc.fundamentos01.core.dtos.ErrorResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.mappers.ProductMapper;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;

@Service
public class ProductServiceImpl implements ProductService {

    private List<ProductModel> products = new ArrayList<>();
    private Long currentId = 1L;

    /*
     * Retorna todos los productos registrados en memoria.
     */
    @Override
    public List<ProductResponseDto> findAll() {
        return products.stream()
                .map(ProductMapper::toResponse)
                .toList();
    }

    /*
     * Busca un producto por id.
     *
     * Si existe, devuelve ProductResponseDto.
     * Si no existe, devuelve ErrorResponseDto.
     */
    @Override
    public Object findOne(Long id) {
        return products.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .map(product -> (Object) ProductMapper.toResponse(product))
                .orElseGet(() -> new ErrorResponseDto("Product not found"));
    }

    /*
     * Crea un nuevo producto.
     *
     * Asigna el id generado en memoria y delega al mapper
     * la asignación de createdAt con LocalDateTime.now().
     */
    @Override
    public ProductResponseDto create(CreateProductDto dto) {
        ProductModel product = ProductMapper.toModel(dto);
        product.setId(currentId);
        currentId++;
        products.add(product);
        return ProductMapper.toResponse(product);
    }

    /*
     * Actualiza completamente un producto existente (PUT).
     *
     * Reemplaza name, price y stock. No modifica id ni createdAt.
     */
    @Override
    public Object update(Long id, UpdateProductDto dto) {
        ProductModel product = products.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null) {
            return new ErrorResponseDto("Product not found");
        }

        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        return ProductMapper.toResponse(product);
    }

    /*
     * Actualiza parcialmente un producto existente (PATCH).
     *
     * Solo actualiza los campos que llegan en el DTO.
     * Los campos nulos se ignoran.
     */
    @Override
    public Object partialUpdate(Long id, PartialUpdateProductDto dto) {
        ProductModel product = products.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (product == null) {
            return new ErrorResponseDto("Product not found");
        }

        if (dto.getName() != null) {
            product.setName(dto.getName());
        }

        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }

        if (dto.getStock() != null) {
            product.setStock(dto.getStock());
        }

        return ProductMapper.toResponse(product);
    }

    /*
     * Elimina un producto por id.
     *
     * Si existe, lo elimina de la lista en memoria.
     * Si no existe, devuelve ErrorResponseDto.
     */
    @Override
    public Object delete(Long id) {
        boolean removed = products.removeIf(product -> product.getId().equals(id));

        if (!removed) {
            return new ErrorResponseDto("Product not found");
        }

        return new ErrorResponseDto("Deleted successfully");
    }
}
