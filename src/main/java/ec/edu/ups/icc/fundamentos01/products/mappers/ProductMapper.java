package ec.edu.ups.icc.fundamentos01.products.mappers;

import java.time.LocalDateTime;

import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.models.ProductModel;

public class ProductMapper {

    /*
     * Convierte un CreateProductDto en un ProductModel.
     *
     * Se usa cuando llega una petición POST para crear un producto.
     * createdAt se asigna aquí con LocalDateTime.now() para que el backend
     * controle la fecha, ignorando cualquier valor que el cliente haya enviado.
     */
    public static ProductModel toModel(CreateProductDto dto) {
        ProductModel model = new ProductModel();
        model.setName(dto.getName());
        model.setPrice(dto.getPrice());
        model.setStock(dto.getStock());
        model.setCreatedAt(LocalDateTime.now());
        return model;
    }

    /*
     * Convierte un ProductModel en un ProductResponseDto.
     *
     * Solo expone los campos que el cliente debe ver.
     * createdAt no se incluye en la respuesta.
     */
    public static ProductResponseDto toResponse(ProductModel model) {
        ProductResponseDto response = new ProductResponseDto();
        response.setId(model.getId());
        response.setName(model.getName());
        response.setPrice(model.getPrice());
        response.setStock(model.getStock());
        return response;
    }
}
