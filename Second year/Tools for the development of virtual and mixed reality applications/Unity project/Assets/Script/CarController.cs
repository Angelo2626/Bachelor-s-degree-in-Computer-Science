using UnityEngine;

// Aggiungiamo questi per assicurarci che i componenti ci siano sempre
[RequireComponent(typeof(Rigidbody))]
[RequireComponent(typeof(Animator))]
public class CarController : MonoBehaviour
{
    [Header("Impostazioni Movimento")]
    [SerializeField] private float moveSpeed = 8f;
    [SerializeField] private float turnSpeed = 120f;

    [Header("Riferimenti Ruote")]
    [SerializeField] private Transform r1; 
    [SerializeField] private Transform r2; 
    [SerializeField] private float maxSteerAngle = 30f;

    // Riferimenti ai componenti e stato interno
    private Rigidbody rb;
    private Animator carAnimator;
    private bool isCarOn = false;
    private Vector3 movement;

    void Start()
    {
        // Ottiene i componenti da QUESTO stesso oggetto
        rb = GetComponent<Rigidbody>();
        carAnimator = GetComponent<Animator>();

        rb.isKinematic = false;
        rb.useGravity = false;
        rb.constraints = RigidbodyConstraints.FreezeRotationX | RigidbodyConstraints.FreezeRotationZ;

        
        carAnimator.SetBool("IsOn", false);
    }

    void Update()
    {
        // Logica per accendere e spegnere la macchina
        if (Input.GetKeyDown(KeyCode.E))
        {
            isCarOn = !isCarOn;
            carAnimator.SetBool("IsOn", isCarOn);
        }

        // Se la macchina è spenta, azzera tutto e interrompi la funzione qui
        if (!isCarOn)
        {
            movement = Vector3.zero;
            carAnimator.SetFloat("Speed", 0);
            HandleWheelSteering(0, 0); // Raddrizza le ruote
            return;
        }

        // --- Se il codice arriva qui, la macchina è ACCESA ---

        // Lettura input per movimento e sterzata
        float verticalInput = Input.GetAxis("Vertical");
        float horizontalInput = Input.GetAxis("Horizontal");

        // Calcolo del vettore di movimento (per la fisica)
        movement = transform.forward * verticalInput * moveSpeed;

        // Rotazione del veicolo (sterzata)
        transform.Rotate(0, horizontalInput * turnSpeed * Time.deltaTime, 0);

        // Comunica la velocità all'Animator
        carAnimator.SetFloat("Speed", Mathf.Abs(verticalInput));

        // Gestisce la rotazione visiva delle ruote
        HandleWheelSteering(horizontalInput, verticalInput);
    }

    void FixedUpdate()
    {
        // Applica il movimento fisico. Sarà (0,0,0) se la macchina è spenta.
        rb.velocity = movement;
    }

    // Funzione helper per le ruote
    void HandleWheelSteering(float horizontalInput, float verticalInput)
    {
        float steerAngle = horizontalInput * maxSteerAngle;
        if (verticalInput < 0)
        {
            steerAngle = -steerAngle;
        }

        // Assumendo che r1 e r2 siano le ruote anteriori che sterzano
        if (r1 != null) r1.localRotation = Quaternion.Euler(0, steerAngle, 0);
        if (r2 != null) r2.localRotation = Quaternion.Euler(0, steerAngle, 0);
    }
}