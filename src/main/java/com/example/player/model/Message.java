package com.example.player.model;

/**
 * senderId id of the sender (for logging)
 */
public record Message(String senderId, String payload) {

}
