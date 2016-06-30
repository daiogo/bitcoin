/*
 * Copyright (C) 2016 diego
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package bitcoin;

import bitcoin.peerClient.MessageSender;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class ObjectSerializer {

    public static byte[] serialize_object(Object object) {
        byte[] serialized_object = null;
        try {
            ObjectOutputStream objectOut = null;
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(object);
            serialized_object = byteOut.toByteArray();
            return serialized_object;
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        return serialized_object;
    }

    public static Object deserialize_object(byte[] message) {
        ObjectInputStream objectIn = null;
        Object object = null;
        try {
            ByteArrayInputStream byteIn = new ByteArrayInputStream(message);
            objectIn = new ObjectInputStream(byteIn);
            object = objectIn.readObject();
            return object;
        } catch (IOException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                objectIn.close();
            } catch (IOException ex) {
                Logger.getLogger(MessageSender.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return object;
    }
    
}
