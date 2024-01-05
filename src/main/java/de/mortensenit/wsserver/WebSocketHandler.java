package de.mortensenit.wsserver;

import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Websocket implementation in Spring.
 * 
 * @author Frederik Mortensen
 */
public class WebSocketHandler extends TextWebSocketHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 */
	@Override
	public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
		super.afterConnectionEstablished(webSocketSession);
	}

	/**
	 * 
	 */
	@Override
	public void handleTextMessage(WebSocketSession session, TextMessage message) {
		logger.debug("Incoming websocket text message: " + message.getPayload());
		sendMessage(session, message.getPayload());
	}

	/**
	 * 
	 */
	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		Charset charset = Charset.forName("UTF-8"); // or other encodings
		String payload = charset.decode(message.getPayload()).toString();
		logger.debug("Incoming websocket binary message: " + payload);
		sendMessage(session, payload);

	}

	/**
	 * 
	 * @param session
	 * @param payload
	 */
	public void sendMessage(WebSocketSession webSocketSession, String payload) {
		try {
			TextMessage msg = new TextMessage(payload);
			webSocketSession.sendMessage(msg);
			logger.debug("Message sent via websocket and with payload: " + payload);
		} catch (IOException e) {
			logger.error("Could not send a websocket message " + payload, e);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) throws Exception {
		super.afterConnectionClosed(webSocketSession, status);
		logger.info("Websocket connection closed: " + status + " - " + webSocketSession.toString());
	}

}