using UnityEngine;

public class CameraSwitch : MonoBehaviour
{
    [Header("Telecamere")]
    public Camera carCamera;    // Telecamera dietro la macchina
    public Camera fpsCamera;    // Telecamera FPS del robot

    [Header("FPS Pivot")]
    public Transform fpsPivot;  // Pivot per la rotazione orizzontale

    [Header("Impostazioni FPS")]
    public float mouseSensitivity = 100f;
    private float xRotation = 0f;  // Angolo di inclinazione verticale

    void Start()
    {
        // Inizia con la telecamera della macchina
        carCamera.enabled = true;
        fpsCamera.enabled = false;
        Cursor.lockState = CursorLockMode.None;
        Cursor.visible = true;

        // Inizializza rotazioni
        xRotation = 0f;
        if (fpsPivot != null)
            fpsPivot.localRotation = Quaternion.identity;
        fpsCamera.transform.localRotation = Quaternion.identity;
    }

    void Update()
    {
        // Cambio telecamera con barra spaziatrice
        if (Input.GetKeyDown(KeyCode.Space))
        {
            SwitchCamera();
        }

        // Controllo solo se la telecamera FPS è attiva
        if (fpsCamera.enabled)
        {
            RotateFPSCamera();
        }
    }

    void SwitchCamera()
    {
        carCamera.enabled = !carCamera.enabled;
        fpsCamera.enabled = !fpsCamera.enabled;

        // Gestione cursore
        if (fpsCamera.enabled)
        {
            Cursor.lockState = CursorLockMode.Locked;
            Cursor.visible = false;
        }
        else
        {
            Cursor.lockState = CursorLockMode.None;
            Cursor.visible = true;
        }
    }

    void RotateFPSCamera()
    {
        // Input del mouse
        float mouseX = Input.GetAxis("Mouse X") * mouseSensitivity * Time.deltaTime;
        float mouseY = Input.GetAxis("Mouse Y") * mouseSensitivity * Time.deltaTime;

        // Rotazione verticale (pitch) sulla camera
        xRotation -= mouseY;
        xRotation = Mathf.Clamp(xRotation, -90f, 90f);
        fpsCamera.transform.localRotation = Quaternion.Euler(xRotation, 0f, 0f);

        // Rotazione orizzontale (yaw) sul pivot
        if (fpsPivot != null)
        {
            fpsPivot.Rotate(Vector3.up * mouseX, Space.Self);
        }
        else
        {
            // Fallback: ruota l'oggetto corrente se il pivot non è assegnato
            transform.Rotate(Vector3.up * mouseX, Space.Self);
        }
    }
}
