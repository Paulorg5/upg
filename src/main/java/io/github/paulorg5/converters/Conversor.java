package io.github.paulorg5.converters;

public interface Conversor<Origem, Destino> {

	Destino converter(Origem origem);

}
