const BASE_URL = 'http://localhost:8080'

export const URLS ={
    SOCKET:'http://localhost:8080/ws',
    FETCH_LOTS:`${BASE_URL}/lot-management/get-lots`,
    GET_PRICE: `${BASE_URL}/lot-management/get-price`,
    RESERVE: `${BASE_URL}/reservation/reserve-spot`,
    USE_SPOT: `${BASE_URL}/reservation/use-spot`
}