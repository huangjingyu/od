package huangjingyu.od.two;

import java.io.Closeable;

public interface ResultWriter extends Closeable {
	void writeHeader();

	void append(Podcast podcast);
}
