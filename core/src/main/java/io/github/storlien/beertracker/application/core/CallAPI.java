package storlien.beertracker.application.core;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;

public class CallAPI {

    private static String bearerToken;
    private static String queryURL = "https://purchase.izettle.com/purchases/v2?limit=1000";

    /**
     * Returnerer JSON-strengen fra et API-kall spesifisert med queryURLending
     *
     * @param queryURLending Slutten på URL-en for å spesifisere API-kallet. F.eks.
     *                       &descending=true eller &startDate=2022-08-01
     * @return JSON-streng med transaksjoner
     */
    public static String getResponseBody(String queryURLending) {
        return getResponse(queryURLending).getBody();
    }

    /**
     * Kaller på Zettle API-et og returnerer svaret. Henter access token fra
     * TokenGetter.getToken().
     *
     * @param queryURLending Slutten på URL-en for å spesifisere API-kallet. F.eks.
     *                       &descending=true eller &startDate=2022-08-01
     * @return HttpReponse<String> fra API. Må bruke .getBody() for å få
     *         JSON-strengen.
     */
    private static HttpResponse<String> getResponse(String queryURLending) {
        HttpResponse<String> response;

        bearerToken = TokenGetter.getToken();

        try {
            response = Unirest.get(queryURL + queryURLending)
                    .header("Authorization", "Bearer " + bearerToken)
                    .asString();
        }

        catch (Exception e) {
            throw new IllegalArgumentException("Failed to get response from Zettle API.");
        }

        return response;

    }
}