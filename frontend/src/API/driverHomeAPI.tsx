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