package com.descodeuses.dto;

import com.descodeuses.voyage.model.Categorie;

public class ConverterDTO {

    public static CategorieDTO convertToDTO(Categorie categorie) {
        /*
         * return CategorieDTO.builder()
         * .nomCategorie(categorie.getNomCategorie())
         * .id(categorie.getId() + "").build();
         */
        CategorieDTO categorieDTO = new CategorieDTO();
        categorieDTO.setId(categorie.getId() + "");
        categorieDTO.setNomCategorie(categorie.getNomCategorie());

        return categorieDTO;
    }
}
