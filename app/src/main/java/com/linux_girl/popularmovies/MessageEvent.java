package com.linux_girl.popularmovies;

public class MessageEvent {

    public final String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public class MessageObject {
        public final MovieObject object;

        public MessageObject(MovieObject object) {
            this.object = object;
        }
    }
}