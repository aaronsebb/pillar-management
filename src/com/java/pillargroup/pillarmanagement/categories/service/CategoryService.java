/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.java.pillargroup.pillarmanagement.categories.service;

import com.java.pillargroup.pillarmanagement.categories.model.Category;
import com.java.pillargroup.pillarmanagement.categories.repository.CategoryRepository;
import com.java.pillargroup.pillarmanagement.exception.ServiceException;
import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService() {
        this.categoryRepository = new CategoryRepository();
    }

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category crearCategoria(String categoryName, String depiction) throws ServiceException {
        if (categoryName == null || categoryName.isBlank()) {
            throw new ServiceException("El nombre de la categoría es obligatorio.");
        }

        Category category = new Category(null, categoryName, depiction);

        try {
            return categoryRepository.save(category);
        } catch (SQLException e) {
            throw new ServiceException("No se pudo crear la categoría.");
        }
    }

    public Category actualizarCategoria(String categoryId, String categoryName, String depiction) throws ServiceException {
        if (categoryId == null || categoryId.isBlank()) {
            throw new ServiceException("Debes indicar qué categoría vas a actualizar.");
        }
        if (categoryName == null || categoryName.isBlank()) {
            throw new ServiceException("El nombre de la categoría es obligatorio.");
        }

        Category category = new Category(categoryId, categoryName, depiction);

        try {
            return categoryRepository.update(category);
        } catch (SQLException e) {
            throw new ServiceException("No se pudo actualizar la categoría: " + e.getMessage());
        }
    }

    public Category obtenerPorId(String categoryId) throws ServiceException {
        try {
            return categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new ServiceException("No existe una categoría con ese ID."));
        } catch (SQLException e) {
            throw new ServiceException("No se pudo buscar la categoría.");
        }
    }

    public List<Category> listarCategorias() throws ServiceException {
        try {
            return categoryRepository.findAll();
        } catch (SQLException e) {
            throw new ServiceException("No se pudieron obtener las categorías.");
        }
    }

    public void eliminarCategoria(String categoryId) throws ServiceException {
        try {
            boolean eliminada = categoryRepository.existsById(categoryId) && categoryRepository.deleteById(categoryId);
            if (!eliminada) {
                throw new ServiceException("No existe una categoría con ese ID.");
            }
        } catch (SQLException e) {
            throw new ServiceException("No se pudo eliminar la categoría.");
        }
    }
}