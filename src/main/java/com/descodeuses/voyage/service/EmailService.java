package com.descodeuses.voyage.service;

public interface EmailService {

    /**
     * 
     *
     * @param fromName    
     * @param fromEmail  
     * @param messageBody 
     */
    void sendContactEmail(String fromName, String fromEmail, String messageBody);

}
