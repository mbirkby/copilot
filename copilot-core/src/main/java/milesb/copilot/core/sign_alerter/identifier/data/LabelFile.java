package milesb.copilot.core.sign_alerter.identifier.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import milesb.copilot.core.sign_alerter.identifier.Label;

public class LabelFile {

	private static final String FORMAT = "UTF-8";

	private List<Label> labels;

	void load(InputStream inputStream) throws IOException {
		labels = new ArrayList<>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, FORMAT));
		try {
			String line = reader.readLine();
			while (line != null) {

				String[] fields = line.split(",");
				if (fields.length == 2) {
					int id = Integer.parseInt(fields[0].trim());
					String text = fields[1].trim();

					Label label = new Label(id, text);
					labels.add(label);
				}

				line = reader.readLine();
			}
		} finally {
			reader.close();
		}

	}

	public List<Label> getLabels() {
		return labels;
	}

	public void setLabels(List<Label> labels) {
		this.labels = labels;
	}

	void save(OutputStream outputStream) throws IOException {

		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, FORMAT));
		try {
			for (Label label : labels) {
				int id = label.getSignTypeId();
				String text = label.getText();
				writer.write(Integer.toString(id));
				writer.write(',');
				writer.write(text.trim());
				writer.newLine();
			}
		} finally {
			writer.flush();
			writer.close();
		}

	}
}
