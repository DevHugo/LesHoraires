package com.amine.horaires.util;

import com.amine.horaires.models.Shop;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

/**
 * Parseur qui permet de parcourir le fichier xml genere
 */
public class Parseur {

    /**
     * Parcours le fichier xml , insere les donnees de l'utilisateur et son
     * historique
     *
     * @return une arraylist de collections
     */
    public ArrayList<Shop> parserShops(String contenuDuFichier) {
        final ArrayList<Shop> listeMagasins = new ArrayList<Shop>();

        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                Shop magasin;

                boolean url = false;
                boolean name = false;
                boolean address = false;
                boolean ville = false;
                boolean zip = false;
                boolean lat = false;
                boolean lng = false;
                boolean tel = false;
                boolean img = false;
                boolean open = false;
                boolean open_status = false;
                boolean pmr = false;
                boolean wifi = false;
                boolean freepark = false;

                public void startElement(String uri, String localName,
                                         String qName, Attributes attributes)
                        throws SAXException {

                    if (qName.equalsIgnoreCase("shop"))
                        magasin = new Shop();

                    if (qName.equalsIgnoreCase("url"))
                        url = true;

                    if (qName.equalsIgnoreCase("name"))
                        name = true;

                    if (qName.equalsIgnoreCase("address"))
                        address = true;

                    if (qName.equalsIgnoreCase("city"))
                        ville = true;

                    if (qName.equalsIgnoreCase("zip"))
                        zip = true;

                    if (qName.equalsIgnoreCase("lat"))
                        lat = true;

                    if (qName.equalsIgnoreCase("lng"))
                        lng = true;

                    if (qName.equalsIgnoreCase("tel"))
                        tel = true;

                    if (qName.equalsIgnoreCase("img"))
                        img = true;

                    if (qName.equalsIgnoreCase("open"))
                        open = true;

                    if (qName.equalsIgnoreCase("open_status"))
                        open_status = true;

                    if (qName.equalsIgnoreCase("pmr"))
                        pmr = true;

                    if (qName.equalsIgnoreCase("pmr"))
                        pmr = true;

                    if (qName.equalsIgnoreCase("wifi"))
                        wifi = true;

                    if (qName.equalsIgnoreCase("freepark"))
                        freepark = true;

                    if (qName.equalsIgnoreCase("shop")) {
                        int length = attributes.getLength();

                        for (int i = 0; i < length; i++) {
                            String nameA = attributes.getQName(i);
                            String valueA = attributes.getValue(i);

                            if (nameA.compareTo("id") == 0) {
                                magasin.setId(Integer.parseInt(valueA));
                            }

                        }
                    }
                }

                public void endElement(String uri, String localName,
                                       String qName) throws SAXException {

                    if (qName.equalsIgnoreCase("shop"))
                        listeMagasins.add(magasin);

                }

                public void characters(char ch[], int start, int length)
                        throws SAXException {

                    if (url) {
                        magasin.setUrl(new String(ch, start, length));
                        url = false;
                    }

                    if (name) {
                        magasin.setName(new String(ch, start, length));
                        name = false;
                    }

                    if (address) {
                        magasin.setAdresse(new String(ch, start, length));
                        address = false;
                    }

                    if (ville) {
                        magasin.setCity(new String(ch, start, length));
                        ville = false;
                    }

                    if (zip) {
                        magasin.setZip(new String(ch, start, length));
                        zip = false;
                    }

                    if (lat) {
                        magasin.setLat(new String(ch, start, length));
                        lat = false;
                    }

                    if (lng) {
                        magasin.setLng(new String(ch, start, length));
                        lng = false;
                    }

                    if (tel) {
                        magasin.setTel(new String(ch, start, length));
                        tel = false;
                    }

                    if (img) {
                        magasin.setHoraires(new String(ch, start, length));
                        img = false;
                    }

                    if (open) {
                        magasin.setOuvert(new String(ch, start, length)
                                .compareTo("1") == 0);
                        open = false;
                    }

                    if (open_status) {
                        magasin.setOpenStatus(new String(ch, start, length));
                        open_status = false;
                    }

                    if (pmr) {
                        magasin.setAccesHandicape(new String(ch, start, length)
                                .compareTo("1") == 0);
                        pmr = false;
                    }

                    if (wifi) {
                        magasin.setWifi(new String(ch, start, length)
                                .compareTo("1") == 0);
                        wifi = false;
                    }

                    if (freepark) {
                        magasin.setParking(new String(ch, start, length)
                                .compareTo("1") == 0);
                        freepark = false;
                    }
                }

            };

            InputStream inputStream = new ByteArrayInputStream(
                    contenuDuFichier.getBytes());
            Reader reader = new InputStreamReader(inputStream, "UTF-8");

            InputSource is = new InputSource(reader);
            is.setEncoding("UTF-8");

            saxParser.parse(is, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listeMagasins;
    }
}