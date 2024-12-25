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
export const getSpotPrice = async(lot_id: number, endTime:any)=>{
    const token = localStorage.getItem('jwtToken');
    const response = await fetch(URLS.GET_PRICE, {
        method: 'POST',
        headers: { 
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token!}`
        },
        body: JSON.stringify({ 
            lotId: lot_id,
            time: endTime
        }),
    });

    return response;
}