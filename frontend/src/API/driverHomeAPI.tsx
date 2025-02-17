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
export const reserveSpot = async(lotId: number, endTime:string)=>{
    const token = localStorage.getItem('jwtToken')
    const response = await fetch(URLS.RESERVE,{
        method:'PUT',
        headers:{
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token!}`
        },
        body:JSON.stringify({
            lotId: lotId,
            endTime: endTime
        })
    })
    return response;
}
export const useSpot = async(lotId:number, spotId:number, payment_method: string)=>{
    const token = localStorage.getItem('jwtToken')
    const response = await fetch(URLS.USE_SPOT,{
        method:'PUT',
        headers:{
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token!}`
        },
        body:JSON.stringify({
            lotId: lotId,
            spotId: spotId,
            payment_method: payment_method
        })
    })
    return response;
}
export const freeSpot = async(lotId:number, spotId:number)=>{
    const token = localStorage.getItem('jwtToken')
    const response = await fetch(URLS.FREE_SPOT,{
        method:'PUT',
        headers:{
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token!}`
        },
        body:JSON.stringify({
            lotId: lotId,
            spotId: spotId,
        })
    })
    return response;
}