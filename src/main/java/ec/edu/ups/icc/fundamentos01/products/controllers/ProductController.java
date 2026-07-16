package ec.edu.ups.icc.fundamentos01.products.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.core.dtos.PaginationDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.CreateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.PartialUpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.ProductResponseDto;
import ec.edu.ups.icc.fundamentos01.products.dtos.UpdateProductDto;
import ec.edu.ups.icc.fundamentos01.products.services.ProductService;
import ec.edu.ups.icc.fundamentos01.security.config.OpenApiConfig;
import ec.edu.ups.icc.fundamentos01.security.services.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@Tag(
    name = "Productos",
    description = "Gestion de productos con paginacion, roles y ownership"
)
@SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME)
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    /*
     * GET /products
     */
    @Operation(
        summary = "Listar todos los productos",
        description = "Devuelve todos los productos sin paginar. Requiere rol ADMIN."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de productos obtenido correctamente"),
        @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene rol ADMIN")
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    /*
     * Endpoint paginado usando Page.
     *
     * GET /products/page
     * GET /products/page?page=0&size=5
     * GET /products/page?page=0&size=5&sortBy=price&direction=desc
     */
    @Operation(
        summary = "Listar productos paginados (Page)",
        description = "Devuelve una página de productos, incluyendo metadata de total de elementos y páginas."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Página de productos obtenida correctamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación u ordenamiento inválidos")
    })
    @GetMapping("/page")
    public Page<ProductResponseDto> findAllPage(@Valid @ModelAttribute PaginationDto pagination) {
        return service.findAllPage(pagination);
    }

    /*
     * Endpoint paginado usando Slice.
     *
     * GET /products/slice
     * GET /products/slice?page=0&size=5
     * GET /products/slice?page=0&size=5&sortBy=createdAt&direction=desc
     */
    @Operation(
        summary = "Listar productos paginados (Slice)",
        description = "Devuelve una porción de productos sin calcular el total, más eficiente que Page."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Slice de productos obtenido correctamente"),
        @ApiResponse(responseCode = "400", description = "Parámetros de paginación u ordenamiento inválidos"),
        @ApiResponse(responseCode = "403", description = "El usuario autenticado no es válido")
    })
    @GetMapping("/slice")
    public Slice<ProductResponseDto> findAllSlice(
            @Valid @ModelAttribute PaginationDto pagination,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return service.findAllSlice(pagination, currentUser);
    }

    /*
     * GET /products/{id}
     */
    @Operation(
        summary = "Obtener un producto por id",
        description = "Devuelve el detalle de un producto según su id."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ProductResponseDto findOne(@PathVariable Long id) {
        return service.findOne(id);
    }

    /*
     * POST /products
     *
     * El owner ya no se toma desde el body.
     * Se obtiene desde el token JWT mediante @AuthenticationPrincipal.
     */
    @Operation(
        summary = "Crear un producto",
        description = "Crea un producto nuevo. El dueño (owner) se obtiene automáticamente del token JWT."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "409", description = "Ya existe un producto con ese nombre")
    })
    @PostMapping
    public ProductResponseDto create(
            @Valid @RequestBody CreateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return service.create(dto, currentUser);
    }

    /*
     * PUT /products/{id}
     */
    @Operation(
        summary = "Actualizar un producto",
        description = "Reemplaza todos los datos de un producto. Solo el dueño del producto puede actualizarlo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "403", description = "El usuario autenticado no es dueño del producto"),
        @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrados")
    })
    @PutMapping("/{id}")
    public ProductResponseDto update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return service.update(id, dto, currentUser);
    }

    /*
     * PATCH /products/{id}
     */
    @Operation(
        summary = "Actualizar parcialmente un producto",
        description = "Actualiza únicamente los campos enviados. Solo el dueño del producto puede actualizarlo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "403", description = "El usuario autenticado no es dueño del producto"),
        @ApiResponse(responseCode = "404", description = "Producto o categoría no encontrados")
    })
    @PatchMapping("/{id}")
    public ProductResponseDto partialUpdate(
            @PathVariable Long id,
            @Valid @RequestBody PartialUpdateProductDto dto,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        return service.partialUpdate(id, dto, currentUser);
    }

    /*
     * DELETE /products/{id}
     */
    @Operation(
        summary = "Eliminar un producto",
        description = "Elimina un producto. Solo el dueño del producto puede eliminarlo."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "403", description = "El usuario autenticado no es dueño del producto"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetailsImpl currentUser) {
        service.delete(id, currentUser);
    }

    /*
     * GET /products/user/{userId}
     */
    @Operation(
        summary = "Listar productos de un usuario",
        description = "Devuelve todos los productos cuyo dueño es el usuario indicado."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de productos obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/user/{userId}")
    public List<ProductResponseDto> findByUserId(
            @Parameter(description = "Id del usuario dueño de los productos", example = "1")
            @PathVariable Long userId) {
        return service.findByUserId(userId);
    }

    /*
     * GET /products/category/{categoryId}
     */
    @Operation(
        summary = "Listar productos de una categoría",
        description = "Devuelve todos los productos que pertenecen a la categoría indicada."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Listado de productos obtenido correctamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/category/{categoryId}")
    public List<ProductResponseDto> findByCategoryId(
            @Parameter(description = "Id de la categoría", example = "1")
            @PathVariable Long categoryId) {
        return service.findByCategoryId(categoryId);
    }
}
