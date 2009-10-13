/*
 *  soapUI, copyright (C) 2004-2009 eviware.com 
 *
 *  soapUI is free software; you can redistribute it and/or modify it under the 
 *  terms of version 2.1 of the GNU Lesser General Public License as published by 
 *  the Free Software Foundation.
 *
 *  soapUI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */

package com.eviware.soapui.impl.wsdl.submit.transports.jms;

import hermes.Domain;
import hermes.Hermes;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import com.eviware.soapui.SoapUI;
import com.eviware.soapui.model.iface.Request;
import com.eviware.soapui.model.iface.Response;
import com.eviware.soapui.model.iface.SubmitContext;
import com.eviware.soapui.model.propertyexpansion.PropertyExpander;

public class HermesJmsRequestPublishTransport extends HermesJmsRequestTransport
{

	public Response execute(SubmitContext submitContext, Request request, long timeStarted) throws Exception
	{
		TopicConnectionFactory connectionFactory = null;
		TopicConnection connection = null;
		TopicSession session = null;
		JMSResponse response = null;
		try
		{
			String topicName = null;
			String sessionName = null;
			String[] parameters = request.getEndpoint().substring(request.getEndpoint().indexOf("://") + 3).split("/");
			if (parameters.length == 2)
			{
				sessionName = PropertyExpander.expandProperties(submitContext, parameters[0]);
				topicName = PropertyExpander.expandProperties(submitContext, parameters[1]).replaceFirst("topic_", "");
			}
			else
				throw new UnresolvedJMSEndpointException("bad jms alias!!!!!");

			submitContext.setProperty(HERMES_SESSION_NAME, sessionName);
			Hermes hermes = getHermes(sessionName, request);
			// connection factory
			connectionFactory = (TopicConnectionFactory) hermes.getConnectionFactory();

			// connection
			connection = connectionFactory.createTopicConnection();
			connection.start();

			// session
			session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);

			// destination
			Topic topic = (Topic) hermes.getDestination(topicName, Domain.TOPIC);

			// publisher
			TopicPublisher messageProducer = session.createPublisher(topic);

			// message
			TextMessage textMessagePublish = session.createTextMessage();
			String messageBody = PropertyExpander.expandProperties(submitContext, request.getRequestContent());
			textMessagePublish.setText(messageBody);

			JMSHeader jmsHeader = new JMSHeader();
			jmsHeader.setMessageHeaders(textMessagePublish, request, hermes, submitContext);
			JMSHeader.setMessageProperties(textMessagePublish, request, hermes, submitContext);

			// publish message to producer
			messageProducer.send(textMessagePublish, textMessagePublish.getJMSDeliveryMode(), textMessagePublish.getJMSPriority(), jmsHeader
					.getTimeTolive());

			// make response
			response = new JMSResponse("", textMessagePublish, null, request, timeStarted);
			submitContext.setProperty(JMS_MESSAGE_SEND, textMessagePublish);
			submitContext.setProperty(JMS_RESPONSE, response);

			return response;
		}
		catch (JMSException jmse)
		{
			SoapUI.logError(jmse);
			submitContext.setProperty(JMS_ERROR, jmse);
			response = new JMSResponse("", null, null, request, timeStarted);
			submitContext.setProperty(JMS_RESPONSE, response);

			return response;
		}
		catch (Throwable t)
		{
			SoapUI.logError(t);
		}
		finally
		{
			// close session and connection
			if (session != null)
				session.close();
			if (connection != null)
				connection.close();
		}
		return null;
	}

}
