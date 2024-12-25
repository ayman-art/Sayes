import { URLS } from "./urls"

export const fetchLots = async(token:string)=>{
    const response = await fetch(URLS.FETCH_LOTS, {
        method:'GET',
        headers:{
            'Authorization': token,
            'Content-Type': 'application/json'
        }
    });
    if (!response.ok){
        console.error("error fetching lots");
        return [];
    }
    const data = await response.json();
    console.log(data);
    return data['lots']
}
export const getSpotPrice = async(lot_id: number)=>{
    const token = localStorage.getItem('jwtToken');
    const response = await fetch('/api/book-spot', {
        method: 'POST',
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token!}`
        },
        body: JSON.stringify({ 
        spotId: lot_id 
        }),
    });

    return response;
}