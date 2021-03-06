package com.ccc.jndi.fetcher.filesystem;

import javax.naming.Context;
import javax.naming.NamingException;

import com.ccc.jndi.context.api.NamedContextFetcher;

public class FileSystemContextFetcherWithName<E> extends FileSystemContextFetcher<E> implements NamedContextFetcher<E> {
	@Override
	public Context fetch() {
		Context ret=super.fetch();
		if(e==null) {
			try {
				e=(E) ret.lookup(name);
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
		}
		return ret;
	}
	
	public E getObject() {
		return e;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	private E e;
	private String name;
}
