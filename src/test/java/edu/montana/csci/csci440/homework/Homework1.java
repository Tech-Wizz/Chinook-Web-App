package edu.montana.csci.csci440.homework;

import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import edu.montana.csci.csci440.DBTest;
import org.junit.jupiter.api.Test;
import spark.embeddedserver.jetty.websocket.WebSocketHandlerWrapper;

import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class Homework1 extends DBTest {

    @Test
    /*
     * Write a query in the string below that returns all artists that have an 'A' in their name
     */
    void selectArtistsWhoseNameHasAnAInIt(){
        List<Map<String, Object>> results = executeSQL("SELECT * FROM artists WHERE name LIKE '%A%'");
        assertEquals(211, results.size());
    }

    @Test
    /*
     * Write a query in the string below that returns all artists that have more than one album
     */
    void selectAllArtistsWithMoreThanOneAlbum(){
        List<Map<String, Object>> results = executeSQL("SELECT *, COUNT(DISTINCT albums.AlbumID) AS albums FROM artists JOIN albums ON artists.ArtistID = albums.ArtistID GROUP BY artists.ArtistID HAVING Albums > 1");

        assertEquals(56, results.size());
        assertEquals("AC/DC", results.get(0).get("Name"));
    }

    @Test
        /*
         * Write a query in the string below that returns all tracks longer than six minutes along with the
         * album and artist name
         */
    void selectTheTrackAndAlbumAndArtistForAllTracksLongerThanSixMinutes() {
        List<Map<String, Object>> results = executeSQL(
                "SELECT tracks.Name as TrackName, " +
                            "tracks.Milliseconds as Milliseconds, " +
                            "albums.Title as AlbumTitle, " +
                            "artists.Name as ArtistsName " +
                        "FROM tracks " +
                            "JOIN albums on tracks.AlbumId = albums.AlbumId " +
                            "JOIN artists on albums.ArtistId = artists.ArtistId " +
                        "WHERE Milliseconds > 360000");

        assertEquals(623, results.size());

        // For now just get the count right, we'll do more elaborate stuff when we get
        // to ORDER BY
        //
        //
//        assertEquals("Princess of the Dawn", results.get(0).get("TrackName"));
//        assertEquals("Restless and Wild", results.get(0).get("AlbumTitle"));
//        assertEquals("Accept", results.get(0).get("ArtistsName"));
//
//        assertEquals("Snoopy's search-Red baron", results.get(10).get("TrackName"));
//        assertEquals("The Best Of Billy Cobham", results.get(10).get("AlbumTitle"));
//        assertEquals("Billy Cobham", results.get(10).get("ArtistsName"));

    }

}
