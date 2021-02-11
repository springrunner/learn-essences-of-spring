package moviebuddy.util;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.util.Collections;

import moviebuddy.ApplicationException;

/**
 * 파일시스템을 다루기 위한 유틸리티 클래스이다.
 * 무비버디 애플리케이션 동작을 보조하기 위해 작성된 유틸리티로 워크숍 또는 강좌에서 별도로 다루지 않스니다.  
 * 따라서 워크숍 참가자 또는 강좌 수강생들 보지 않으셔도 됩니다.   
 * 
 * @author springrunner.kr@gmail.com
 */
public class FileSystemUtils {

	/*
	 * JAR(Java Archive, 자바 아카이브)로 패키징된 애플리케이션에서 파일시스템 접근시 필요에 따라 파일시스템을 초기화한다.
	 */
	public static URI checkFileSystem(URI uri) {
		if("jar".equalsIgnoreCase(uri.getScheme())){
		    for (FileSystemProvider provider : FileSystemProvider.installedProviders()) {
		        if (provider.getScheme().equalsIgnoreCase("jar")) {
		            try {
		                provider.getFileSystem(uri);
		            } catch (FileSystemNotFoundException ignore) {
		                // in this case we need to initialize it first
		                try {
							provider.newFileSystem(uri, Collections.emptyMap());
						} catch (IOException error) {
							throw new ApplicationException("failed to initialize file system.", error);
						}
		            }
		        }
		    }
		}
		return uri;
	}
	
}
