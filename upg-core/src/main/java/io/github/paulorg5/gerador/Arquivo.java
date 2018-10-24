package io.github.paulorg5.gerador;

import java.io.InputStream;

public interface Arquivo {

	String getCaminhoNoZip();

	InputStream getInputStream();
}
