package com.fatguy.behealthy.Models.gmap;

public class JSONgmap {
    private String[] html_attributions;
    private results[] results;
    private String status;

    public int counter() {
        return results.length;
    }

    public JSONgmap() {
        results = null;
        status = "";
        html_attributions = null;
    }

    public JSONgmap(String[] html_attributions, com.fatguy.behealthy.Models.gmap.results[] results, String status) {
        this.html_attributions = html_attributions;
        this.results = results;
        this.status = status;
    }

    public String[] getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(String[] html_attributions) {
        this.html_attributions = html_attributions;
    }

    public com.fatguy.behealthy.Models.gmap.results[] getResults() {
        return results;
    }

    public void setResults(com.fatguy.behealthy.Models.gmap.results[] results) {
        this.results = results;
    }

    public void setNewResults(int length) {
        this.results = new results[length];
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
