const URL = "http://localhost:8080"
export const loginUser = async (name: string, password: string) => {
    const response = await fetch(`${URL}/users/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ 
        "name":name,
        "password":password,
      }),
    });
  
    if (response.ok) {
      const data = await response.json();
      const token = data['jwt']
      console.log(token)
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', token);
    } else {
      throw new Error('Login failed');
    }
  };
  
  export const registerDriver = async (
    name: string,
    password: string,
    role: string,
    plateNumber: string,
    licenseNumber: string
  ) => {
    const response = await fetch(`${URL}/users/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ 
        "name":name, 
        "password":password,
        "role":role,
        "plateNumber": plateNumber,
        "licenseNumber": licenseNumber 
      }),
    });
  
    if (response.ok ) {
      const data = await response.json();
      const token = data['jwt']
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', token);
    } else {
      throw new Error('Registration failed');
    }
  };

  export const registerLotManager = async (name: string, password: string, role: string) => {
    const response = await fetch(`${URL}/users/register`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({ 
        "name":name,
        "password":password,
        "role":role 
      }),
    });
  
    
    if (response.ok ) {
      // Save JWT to localStorage
      const token = await response.json()
      localStorage.setItem('jwtToken', token['jwt']);
    } else {
      throw new Error('Registration failed');
    }

  };

  
  // Utility to check if the user is authenticated by validating the JWT
  export const getToken = (): string | null => {
    return localStorage.getItem('jwtToken');
  };
  
  export const logoutUser = () => {
    // Remove the JWT from localStorage
    localStorage.removeItem('jwtToken');
  };
  export const authorizeToken = async( token: string)=>{
    console.log(token)
    const response = await fetch(`${URL}/users/auth`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
    });
    if (!response.ok) {
      throw new Error(`Failed to authenticate token`);
    }

    const data = await response.json();

    return data;
  }

export const saveData = (token:string)=>{
    console.log(token)
    const [header, payload, signature] = token.split('.');

    // Decode payload
    const decodedPayload = base64urlDecode(payload);

    // Parse the payload into a JavaScript object
    const decodedObj = JSON.parse(decodedPayload);
    localStorage.setItem("jwtToken", token);
    localStorage.setItem("role", decodedObj.role)
    localStorage.setItem("name", decodedObj.sub)
    localStorage.setItem("id", decodedObj.jti)
}

function base64urlDecode(base64url: string) {
    // Base64url to Base64
    let base64 = base64url.replace(/-/g, '+').replace(/_/g, '/');
    // Decode the Base64 string
    let decodedData = atob(base64);
    return decodedData;
  }

  export const clearData = ()=>{
    localStorage.removeItem("role")
    localStorage.removeItem("name")
    localStorage.removeItem("id")
    localStorage.removeItem("jwtToken")
}