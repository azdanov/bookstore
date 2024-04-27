package dev.azdanov.catalogservice.domain;

class ProductMapper {

    private ProductMapper() {}

    static Product toProduct(ProductEntity productEntity) {
        return new Product(
                productEntity.getCode(),
                productEntity.getName(),
                productEntity.getDescription(),
                productEntity.getImageUrl(),
                productEntity.getPrice());
    }
}
