package de.jthedroid.whatsappchatanalyzer;

interface DataProvider {
    ChatData getData(String key);

    void putData(String key, ChatData chatData);
}

interface ChatData {
}