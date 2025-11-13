package com.descodeuses.voyage.Controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.boot.test.mock.mockito.MockBean;
import com.descodeuses.voyage.service.CategorieService;
import com.descodeuses.voyage.service.UtilisateurService;
import com.descodeuses.voyage.service.VideoService;
import com.descodeuses.voyage.controller.AdminController;
import com.descodeuses.voyage.repository.CategorieRepository;
import com.descodeuses.voyage.service.JpaUserDetailsService;


import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@WebMvcTest(AdminController.class)
public class AdminControllerTest {

  
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategorieService categorieService;
    @MockBean
    private VideoService videoService;
    @MockBean
    private UtilisateurService utilisateurService;
    @MockBean
    private CategorieRepository categorieRepository;
    @MockBean
    private JpaUserDetailsService jpaUserDetailsService; 

    @Test 
    @WithMockUser(roles = "ADMIN") 
    public void testPageAdmin_QuandAdminConnecte_DoitRetournerVueAdmin() throws Exception {
        
      
        mockMvc.perform(get("/Admin"))
            
           
            .andExpect(status().isOk()) 
            .andExpect(view().name("admin")); 
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testPageLesVideos_QuandAdminConnecte_DoitRetournerVueAllVideo() throws Exception {
        
       
        mockMvc.perform(get("/lesVideos"))
        
      
            .andExpect(status().isOk()) 
            .andExpect(view().name("allVideo")); 
    }
}