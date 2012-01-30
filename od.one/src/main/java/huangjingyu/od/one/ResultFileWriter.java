package huangjingyu.od.one;

import java.io.Closeable;

public interface ResultFileWriter extends Closeable {
	void writeHeader();

	void append(Restaurant restaurant);
}
