package milesb.copilot.core.sign_alerter.identifier.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

class MatIO {

	void writeMat(Mat mat, DataOutputStream dos) throws IOException {

		dos.writeInt(mat.rows());
		dos.writeInt(mat.cols());
		dos.writeInt(mat.type());

		int depth = mat.depth();

		if (depth == CvType.CV_8S || depth == CvType.CV_8U) {
			writeBytes(mat, dos);
		} else if (depth == CvType.CV_16S || depth == CvType.CV_16U) {
			writeShorts(mat, dos);
		} else if (depth == CvType.CV_32S) {
			writeInts(mat, dos);
		} else if (depth == CvType.CV_32F) {
			writeFloats(mat, dos);
		} else if (depth == CvType.CV_64F) {
			writeDoubles(mat, dos);
		}

	}

	Mat readMat(DataInputStream dis) throws IOException {
		int rows = dis.readInt();
		int cols = dis.readInt();
		int type = dis.readInt();

		Mat mat = new Mat(rows, cols, type);
		int depth = mat.depth();

		if (depth == CvType.CV_8S || depth == CvType.CV_8U) {
			readBytes(mat, dis);
		} else if (depth == CvType.CV_16S || depth == CvType.CV_16U) {
			readShorts(mat, dis);
		} else if (depth == CvType.CV_32S) {
			readInts(mat, dis);
		} else if (depth == CvType.CV_32F) {
			readFloats(mat, dis);
		} else if (depth == CvType.CV_64F) {
			readDoubles(mat, dis);
		}

		return mat;

	}

	private void writeBytes(Mat mat, DataOutputStream dos) throws IOException {
		byte[] values = new byte[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {
				mat.get(row, col, values);
				for (int channel = 0; channel < mat.channels(); channel++) {
					dos.writeByte(values[channel]);
				}
			}
		}

	}

	private void readBytes(Mat mat, DataInputStream dos) throws IOException {
		byte[] values = new byte[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {

				for (int channel = 0; channel < mat.channels(); channel++) {
					values[channel] = dos.readByte();
				}

				mat.put(row, col, values);
			}
		}

	}

	private void writeShorts(Mat mat, DataOutputStream dos) throws IOException {
		short[] values = new short[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {
				mat.get(row, col, values);
				for (int channel = 0; channel < mat.channels(); channel++) {
					dos.writeShort(values[channel]);
				}
			}
		}

	}

	private void readShorts(Mat mat, DataInputStream dos) throws IOException {
		short[] values = new short[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {

				for (int channel = 0; channel < mat.channels(); channel++) {
					values[channel] = dos.readShort();
				}

				mat.put(row, col, values);
			}
		}

	}

	private void writeInts(Mat mat, DataOutputStream dos) throws IOException {
		int[] values = new int[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {
				mat.get(row, col, values);
				for (int channel = 0; channel < mat.channels(); channel++) {
					dos.writeInt(values[channel]);
				}
			}
		}

	}

	private void readInts(Mat mat, DataInputStream dos) throws IOException {
		int[] values = new int[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {

				for (int channel = 0; channel < mat.channels(); channel++) {
					values[channel] = dos.readInt();
				}

				mat.put(row, col, values);
			}
		}

	}

	private void writeFloats(Mat mat, DataOutputStream dos) throws IOException {
		float[] values = new float[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {
				mat.get(row, col, values);
				for (int channel = 0; channel < mat.channels(); channel++) {
					dos.writeFloat(values[channel]);
				}
			}
		}

	}

	private void readFloats(Mat mat, DataInputStream dos) throws IOException {
		float[] values = new float[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {

				for (int channel = 0; channel < mat.channels(); channel++) {
					values[channel] = dos.readFloat();
				}

				mat.put(row, col, values);
			}
		}

	}

	private void writeDoubles(Mat mat, DataOutputStream dos) throws IOException {
		double[] values = new double[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {
				mat.get(row, col, values);
				for (int channel = 0; channel < mat.channels(); channel++) {
					dos.writeDouble(values[channel]);
				}
			}
		}

	}

	private void readDoubles(Mat mat, DataInputStream dos) throws IOException {
		double[] values = new double[mat.channels()];
		for (int row = 0; row < mat.rows(); row++) {
			for (int col = 0; col < mat.cols(); col++) {

				for (int channel = 0; channel < mat.channels(); channel++) {
					values[channel] = dos.readDouble();
				}

				mat.put(row, col, values);
			}
		}

	}
}
