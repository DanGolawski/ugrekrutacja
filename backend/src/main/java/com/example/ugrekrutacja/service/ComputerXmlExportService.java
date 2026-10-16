
package com.example.ugrekrutacja.service;

import com.example.ugrekrutacja.entity.Computer;
import com.example.ugrekrutacja.repository.ComputerRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ComputerXmlExportService {

    private final ComputerRepository computerRepository;

    public ComputerXmlExportService(
            ComputerRepository computerRepository
    ) {
        this.computerRepository = computerRepository;
    }

    public byte[] exportAll() {
        List<Computer> computers = computerRepository.findAll(
                Sort.by(Sort.Direction.ASC, "id")
        );

        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            XMLStreamWriter writer = XMLOutputFactory.newFactory()
                    .createXMLStreamWriter(output, "UTF-8");

            try {
                writer.writeStartDocument("UTF-8", "1.0");
                writer.writeCharacters("\n");
                writer.writeStartElement("faktura");

                for (Computer computer : computers) {
                    writer.writeCharacters("\n    ");
                    writer.writeStartElement("komputer");

                    writeElement(writer, "nazwa", computer.getName());
                    writeElement(
                            writer,
                            "data_ksiegowania",
                            computer.getAccountingDate().toString()
                    );
                    writeElement(
                            writer,
                            "koszt_USD",
                            computer.getAmountUsd().toPlainString()
                    );
                    writeElement(
                            writer,
                            "koszt_PLN",
                            computer.getAmountPln().toPlainString()
                    );

                    writer.writeCharacters("\n    ");
                    writer.writeEndElement();
                }

                if (!computers.isEmpty()) {
                    writer.writeCharacters("\n");
                }

                writer.writeEndElement();
                writer.writeCharacters("\n");
                writer.writeEndDocument();
                writer.flush();
            } finally {
                writer.close();
            }

            return output.toByteArray();
        } catch (XMLStreamException ex) {
            throw new IllegalStateException(
                    "Nie udało się wygenerować pliku XML",
                    ex
            );
        } catch (java.io.IOException ex) {
            throw new IllegalStateException(
                    "Nie udało się przygotować pliku XML",
                    ex
            );
        }
    }

    private void writeElement(
            XMLStreamWriter writer,
            String elementName,
            String value
    ) throws XMLStreamException {
        writer.writeCharacters("\n        ");
        writer.writeStartElement(elementName);
        writer.writeCharacters(value);
        writer.writeEndElement();
    }
}
