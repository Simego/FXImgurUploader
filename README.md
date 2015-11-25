# FXImgurUploader

The new **ImgurUploader** is made with **JavaFX** (no swing), uses **Java 8** but is a little faster, a lot better and much prettier, many improvements to be made since it's not finished and code to be rewritten.

* **Java 8 needed**: https://www.java.com/download
* **Latest Release**: [ImgurUploader Beta v0.4.0](https://github.com/Simego/FXImgurUploader/releases/tag/v0.4.0-beta)

* **Please report issues and improvements/ideas in the [> Issues <](https://github.com/Simego/FXImgurUploader/issues) section, thanks!!**

*(this project started as a test so with time many stuff will be rewritten, for example the main class holds everything of the main application and UI/config building)*

# Functionalities
* Authentication with Imgur API, now you can use your own account to keep your uploads!
* PrintScreen binding to save screenshots.
* Give a description (title) to images you're about to upload or just to keep track.
* Drag and Drop URLs or Images to import to the ImgurUploader (to just save or upload).
* Images can be deleted from the table pressing DELETE on the keyboard (with confirmation).

# Preview
![](http://i.imgur.com/PeyesgA.png)
![](http://i.imgur.com/66UyHTI.png)

### Changelog
#### v0.4.0
* Native Dialogs from JavaFX 8.4+ (fixes for Java 8.6).
* Dialogs have a nicer look now (imgur-like style).

#### v0.3.0
* Now you can delete photos from the table, if it's uploaded the image will be deleted from Imgur.

#### v0.2.0
* Added drag and drop support for urls and images.
* Now while uploading the screen will be locked until it finishes.
* Starting internationalization support with ResourceBundles.

#### v0.1.0
* First version, created everything, many stuff to be improved.
