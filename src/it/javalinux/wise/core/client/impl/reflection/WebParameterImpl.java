/**
 *  WISE Invokes Services Easily - Stefano Maestri / Alessio Soldano
 *  
 *  http://www.javalinuxlabs.org - http://www.javalinux.it 
 *
 *  Wise is free software; you can redistribute it and/or modify it under the 
 *  terms of the GNU Lesser General Public License as published by the Free Software Foundation; 
 *  either version 2.1 of the License, or (at your option) any later version.
 *
 *  Wise is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without 
 *  even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 *  See the GNU Lesser General Public License for more details at gnu.org.
 */
package it.javalinux.wise.core.client.impl.reflection;

import it.javalinux.wise.core.client.WebParameter;
import java.lang.reflect.Type;
import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import net.jcip.annotations.Immutable;

/**
 * Holds single parameter's data required for an invocation
 * 
 * @author stefano.maestri@javalinux.it
 */
@Immutable
public class WebParameterImpl implements WebParameter {

    private final Type type;

    private final String name;

    private final int position;

    private final Enum<WebParam.Mode> mode;

    /**
     * @param type
     * @param name
     * @param position
     * @param mode
     */
    public WebParameterImpl( Type type,
                             String name,
                             int position,
                             Enum<Mode> mode ) {
        super();
        this.type = type;
        this.name = name;
        this.position = position;
        this.mode = mode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.wise.core.client.WebParameter#getMode()
     */
    public Enum<Mode> getMode() {
        return mode;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.wise.core.client.WebParameter#getName()
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.wise.core.client.WebParameter#getPosition()
     */
    public int getPosition() {
        return position;
    }

    /**
     * {@inheritDoc}
     * 
     * @see it.javalinux.wise.core.client.WebParameter#getType()
     */
    public Type getType() {
        return type;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mode == null) ? 0 : mode.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + position;
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof WebParameterImpl)) return false;
        final WebParameterImpl other = (WebParameterImpl)obj;
        if (mode == null) {
            if (other.mode != null) return false;
        } else if (!mode.equals(other.mode)) return false;
        if (name == null) {
            if (other.name != null) return false;
        } else if (!name.equals(other.name)) return false;
        if (position != other.position) return false;
        if (type == null) {
            if (other.type != null) return false;
        } else if (!type.equals(other.type)) return false;
        return true;
    }

}
