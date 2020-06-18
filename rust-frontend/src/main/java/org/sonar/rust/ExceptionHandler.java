package org.sonar.rust;

import com.sonar.sslr.api.RecognitionException;

/**
 * Interface defining how a rust check should react when errors are occurring during analysis.
 */
public interface ExceptionHandler {

    void processRecognitionException(RecognitionException e);

    void processException(Exception e);

}
