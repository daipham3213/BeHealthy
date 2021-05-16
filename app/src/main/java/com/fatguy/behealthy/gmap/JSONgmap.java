package com.fatguy.behealthy.gmap;

public class JSONgmap {
    private String[] html_attributions;
    private results[] results;
    private String status;

    public JSONgmap() {
    }

    public JSONgmap(String[] html_attributions, com.fatguy.behealthy.gmap.results[] results, String status) {
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

    public com.fatguy.behealthy.gmap.results[] getResults() {
        return results;
    }

    public void setResults(com.fatguy.behealthy.gmap.results[] results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
