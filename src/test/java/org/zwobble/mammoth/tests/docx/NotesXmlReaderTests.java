package org.zwobble.mammoth.tests.docx;

import org.junit.Test;
import org.zwobble.mammoth.Result;
import org.zwobble.mammoth.documents.Note;
import org.zwobble.mammoth.docx.BodyXmlReader;
import org.zwobble.mammoth.docx.NotesXmlReader;
import org.zwobble.mammoth.xml.XmlElement;

import java.util.List;

import static com.natpryce.makeiteasy.MakeItEasy.a;
import static com.natpryce.makeiteasy.MakeItEasy.make;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.zwobble.mammoth.Result.success;
import static org.zwobble.mammoth.tests.DeepReflectionMatcher.deepEquals;
import static org.zwobble.mammoth.tests.documents.DocumentElementMakers.PARAGRAPH;
import static org.zwobble.mammoth.util.MammothLists.list;
import static org.zwobble.mammoth.util.MammothMaps.map;
import static org.zwobble.mammoth.xml.XmlNodes.element;

public class NotesXmlReaderTests {
    private static final BodyXmlReader bodyReader = make(a(BodyXmlReaderMakers.bodyReader));

    @Test
    public void idAndBodyOfFootnoteAreRead() {
        XmlElement element = element("w:footnotes", list(
            element("w:footnote", map("w:id", "1"), list(
                element("w:p")))));

        NotesXmlReader reader = new NotesXmlReader(bodyReader, "footnote");
        Result<List<Note>> notes = reader.readElement(element);

        assertThat(
            notes,
            deepEquals(success(list(new Note("1", list(make(a(PARAGRAPH))))))));
    }

    @Test
    public void continuationSeparatorIsIgnored() {
        assertFootnoteTypeIsIgnored("continuationSeparator");
    }

    @Test
    public void separatorIsIgnored() {
        assertFootnoteTypeIsIgnored("separator");
    }

    private void assertFootnoteTypeIsIgnored(String noteType) {
        XmlElement element = element("w:footnotes", list(
            element("w:footnote", map("w:id", "1", "w:type", noteType), list(
                element("w:p")))));

        NotesXmlReader reader = new NotesXmlReader(bodyReader, "footnote");
        Result<List<Note>> notes = reader.readElement(element);

        assertThat(notes, deepEquals(success(list())));
    }
}