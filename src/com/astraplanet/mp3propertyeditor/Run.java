package com.astraplanet.mp3propertyeditor;

import java.io.*;
import java.util.*;

import com.mpatric.mp3agic.*;

public class Run {
	public static void main(String [] args) {
		if (args.length < 3) {
			System.out.println("3 arguments are required: 1) File path 2) String to search 3)String to replace with");
			return;
		}
		String path = args[0];
		String findString = args[1];
		String replace = args[2];
		
		String[] findArray = findString.split("[,]");
		
		List<String> files = getFilesInDirectory(path);
		
		for(String file: files) {
			try {
				if (file.indexOf(".mp3") < 0) {
					continue;
				}
				
				String oldFile = file;
				
				Mp3File mp3file = new Mp3File(file);
				if (mp3file.hasId3v1Tag()) {
					ID3v1 id3v1Tag = mp3file.getId3v1Tag();
					
					String artist = id3v1Tag.getArtist();
					String title = id3v1Tag.getTitle();
					String album = id3v1Tag.getAlbum();
					
					for (int i = 0; i < findArray.length; i++) {
						String find = findArray[i];
						
						artist = artist.replace(find, replace);
					  	title = title.replace(find, replace);
					  	album = album.replace(find, replace);
					}
				  	
				  	id3v1Tag.setArtist(artist);
				  	id3v1Tag.setTitle(title);
				  	id3v1Tag.setAlbum(album);
				  	id3v1Tag.setComment(""); 	
				}
				
				if (mp3file.hasId3v2Tag()) {
					ID3v2 id3v2Tag = mp3file.getId3v2Tag();
					
					String artist = id3v2Tag.getArtist();
					String title = id3v2Tag.getTitle();
					String album = id3v2Tag.getAlbum();
				  	
					for (int i = 0; i < findArray.length; i++) {
						String find = findArray[i];
						
						artist = artist.replace(find, replace);
					  	title = title.replace(find, replace);
					  	album = album.replace(find, replace);
					}
				  	
				  	id3v2Tag.setArtist(artist);
				  	id3v2Tag.setTitle(title);
				  	id3v2Tag.setAlbum(album);
				  	id3v2Tag.setComment("");
				}
				  	
			  	String newfile = file;
			  	
			  	for (int i = 0; i < findArray.length; i++) {
					String find = findArray[i];
					
					newfile = newfile.replace(find, replace);
				}
			  	
			  	if (!newfile.equals(file)) {
			  		mp3file.save(newfile);
			  	} else if (newfile.indexOf("-1") > -1) {
		  			newfile = newfile.replace("-1", "");
		  			mp3file.save(newfile);
		  		} else {
		  			mp3file.save(newfile + "-1");
			  	}
			
				new File(oldFile).delete();
			} catch (UnsupportedTagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidDataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static List<String> getFilesInDirectory(String path) {
		File folder = new File(path);
		List<String> files = listFilesForFolder(folder);
		return files;
	}
	
	public static List<String> listFilesForFolder(final File folder) {
		List<String> files = new ArrayList<String>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            listFilesForFolder(fileEntry);
	        } else {
	            files.add(fileEntry.getAbsolutePath());
	        }
	    }
	    return files;
	}
}
