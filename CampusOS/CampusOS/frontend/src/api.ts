const BASE = import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1';
export async function api<T>(path:string, init?:RequestInit):Promise<T>{
  const res=await fetch(`${BASE}${path}`,{...init,headers:{'Content-Type':'application/json',...(init?.headers||{})}});
  if(!res.ok){let msg=`Request failed (${res.status})`;try{const body=await res.json();msg=body.message||msg}catch{}throw new Error(msg)}
  return res.status===204?undefined as T:res.json();
}
