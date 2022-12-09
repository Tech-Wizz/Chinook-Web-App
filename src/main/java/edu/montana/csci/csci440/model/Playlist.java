package edu.montana.csci.csci440.model;

import edu.montana.csci.csci440.util.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Playlist extends Model {

    Long playlistId;
    String name;

    public Playlist() {
    }

    public Playlist(ResultSet results) throws SQLException {
        name = results.getString("Name");
        playlistId = results.getLong("PlaylistId");
    }

    public static List<Playlist> forTracks(long trackId){
        try(Connection connection = DB.connect();
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT playlists.PlaylistId, playlists.Name FROM tracks\n" +
                            "JOIN playlist_track ON tracks.TrackId = playlist_track.TrackId\n" +
                            "JOIN playlist ON playlist_track.PlaylistId = playlist.PlaylistId\n" +
                            "where tracks.TrackId = " + trackId

            )
        ){
            ResultSet results = statement.executeQuery();
            List<Playlist> resultList = new LinkedList<>();
            while (results.next()){
                resultList.add(new Playlist(results));
            }
            return resultList;
        }catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }


    public List<Track> getTracks(){
        // TODO implement, order by track name
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM tracks JOIN playlist_track on playlist_track.TrackId=tracks.TrackId JOIN playlists on playlists.PlaylistId=playlist_track.PlaylistId WHERE playlists.PlaylistId=? ORDER By tracks.name")) {
            stmt.setLong(1, this.getPlaylistId());
            ResultSet results = stmt.executeQuery();
            List<Track> resultList= new LinkedList<>();
            while (results.next()) {
                resultList.add(new Track(results));
            } return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
    public Long getPlaylistId() {
        return playlistId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Playlist> all() {
        return all(0, Integer.MAX_VALUE);
    }

    public static List<Playlist> all(int page, int count) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT * FROM playlists LIMIT ? OFFSET ?"
             )) {
            stmt.setInt(1, count);
            stmt.setInt(2, (page-1)*count);
            ResultSet results = stmt.executeQuery();
            List<Playlist> resultList = new LinkedList<>();
            while (results.next()) {
                resultList.add(new Playlist(results));
            }
            return resultList;
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public static Playlist find(int i) {
        try (Connection conn = DB.connect();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM playlists WHERE PlaylistId=?")) {
            stmt.setLong(1, i);
            ResultSet results = stmt.executeQuery();
            if (results.next()) {
                return new Playlist(results);
            } else {
                return null;
            }
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

}