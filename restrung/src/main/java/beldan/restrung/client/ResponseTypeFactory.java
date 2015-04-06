package beldan.restrung.client;

import beldan.restrung.marshalling.response.JSONResponse;


public interface ResponseTypeFactory {

    <T extends JSONResponse> T newInstance(Class<T> targetClass);

}
