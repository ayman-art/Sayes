const URL = "http://localhost:8080"
export const loginUser = async (name: string, password: string, role: string) => {
    const response = await fetch(`${URL}/users/login`, {
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
  
    const data = await response.json();
    console.log(data)
    if (response.ok && data.token) {
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', data.token);
    } else {
      throw new Error('Login failed');
    }
    return data;
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
  
    const data = await response.json();
    if (response.ok && data.token) {
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', data.token);
    } else {
      throw new Error('Registration failed');
    }
    return data;
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
  
    const data = await response.json();
    if (response.ok && data.token) {
      // Save JWT to localStorage
      localStorage.setItem('jwtToken', data.token);
    } else {
      throw new Error('Registration failed');
    }
    return data;
  };

  
  // Utility to check if the user is authenticated by validating the JWT
  export const getToken = (): string | null => {
    return localStorage.getItem('jwtToken');
  };
  
  export const logoutUser = () => {
    // Remove the JWT from localStorage
    localStorage.removeItem('jwtToken');
  };
  